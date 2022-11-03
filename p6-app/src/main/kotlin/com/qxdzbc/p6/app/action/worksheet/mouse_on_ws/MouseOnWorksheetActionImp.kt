package com.qxdzbc.p6.app.action.worksheet.mouse_on_ws

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorText
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import javax.inject.Inject


class MouseOnWorksheetActionImp @Inject constructor(
    private val appStateMs: Ms<AppState>,
    private val clickOnCell: ClickOnCell,
    private val makeDisplayText: MakeCellEditorDisplayTextAction,
    private val updateRangeSelectorText: UpdateRangeSelectorText,
) : MouseOnWorksheetAction, ClickOnCell by clickOnCell {

    private val appState by appStateMs

    override fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWs) {
        appState.getWsState(cursorLoc)?.also { wsState->
            var cursorState by wsState.cursorStateMs
            if (cellAddress != cursorState.mainCell) {
                cursorState = cursorState.setMainRange(RangeAddresses.from2Cells(cellAddress, cursorState.mainCell))
            }
            updateRangeSelectorText.updateRangeSelectorText()
        }
    }

    /**
     * this also handle click-on-cell action
     */
    override fun startDragSelection(
        wbws: WbWs,
        anchorCell: CellAddress
    ) {
        clickOnCell(anchorCell, wbws)
    }

    override fun startDragSelection(
        wbws: WbWs,
        mousePosition: Offset,
        offset: Offset,
    ) {
        this.appState.getWsState(wbws)?.also { wsState ->
            var selectRect by wsState.selectRectStateMs
            selectRect = selectRect
                .setAnchorPoint(mousePosition)
                .activate()
            for ((cellAddress, layout) in wsState.cellLayoutCoorMap) {
                val cellRect = layout.boundInWindowOrZero
                if (cellRect.contains(selectRect.anchorPoint)) {
                    startDragSelection(wsState, cellAddress)
                    break
                }
            }
        }
    }

    override fun stopDragSelection(cursorLocation: WbWs) {
        appState.getWsState(cursorLocation)?.also { wsState ->
            var selectRect by wsState.selectRectStateMs
            selectRect = selectRect.hide().deactivate()
        }
    }

    override fun makeMouseDragSelectionIfPossible(
        cursorLocation: WbWs,
        currentCellMouseOn: CellAddress,
    ) {
        appState.getCursorStateMs(cursorLocation)?.also {
            val cursorState by it
            val newRange = RangeAddresses.fromManyCells(listOf(currentCellMouseOn, cursorState.mainCell))
            val newCursorState = cursorState.setMainRange(newRange)
            it.value = newCursorState
            updateRangeSelectorText.updateRangeSelectorText()
        }
    }

    override fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, mousePosition: Offset, offset: Offset) {
        appState.getWsState(cursorLocation)?.also { wsState ->
            val selectRect by wsState.selectRectStateMs
            if (selectRect.isActive) {
                wsState.selectRectStateMs.value = selectRect.setMovingPoint(mousePosition).show()
                val currentCellMouseOn:CellAddress? = wsState.cellLayoutCoorMap.entries.firstOrNull { (_, layout) ->
                    val cellRect = layout.boundInWindowOrZero
                    cellRect.contains(mousePosition)
                }?.key
                if (currentCellMouseOn != null) {
                    makeMouseDragSelectionIfPossible(wsState, currentCellMouseOn)
                }
            }
        }
    }

    private fun f(cellAddress: CellAddress, cursorLoc: WbWs) {
        appState.getWsState(cursorLoc)?.also { wsState ->
            var cursorState by wsState.cursorStateMs
            if (cellAddress != cursorState.mainCell) {
                if (cursorState.isPointingTo(cellAddress)) {
                    var newCursor = cursorState
                    if (cellAddress in newCursor.fragmentedCells) {
                        newCursor = newCursor.removeFragCell(cellAddress)
                    }
                    if (newCursor.mainRange?.contains(cellAddress) == true) {
                        val brokenRanges = newCursor.mainRange?.removeCell(cellAddress) ?: emptyList()
                        newCursor = newCursor
                            .removeMainRange()
                            .addFragRanges(brokenRanges)
                    }
                    for (range in newCursor.fragmentedRanges) {
                        val brokenRanges = range.removeCell(cellAddress)
                        newCursor = newCursor
                            .removeFragRange(range)
                            .addFragRanges(brokenRanges)
                    }
                    cursorState = newCursor
                } else {
                    cursorState = cursorState.addFragCell(cellAddress)
                }
            }
        }
    }


    override fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWs) {
        val ce by appState.cellEditorStateMs
        if (ce.isActiveAndAllowRangeSelector) {
            return
        }
        if (ce.isActive && !ce.allowRangeSelector) {
            appState.cellEditorState = ce.close()
            f(cellAddress, cursorLoc)
            return
        }
        if (!ce.isActive && !ce.allowRangeSelector) {
            f(cellAddress, cursorLoc)
        }
    }
}
