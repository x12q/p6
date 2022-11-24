package com.qxdzbc.p6.app.action.worksheet.mouse_on_ws

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorText
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell.ClickOnCell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=MouseOnWorksheetAction::class)
class MouseOnWorksheetActionImp @Inject constructor(
    private val scMs: Ms<StateContainer>,
    private val clickOnCell: ClickOnCell,
    private val makeDisplayText: MakeCellEditorTextAction,
    private val updateRangeSelectorText: UpdateRangeSelectorText,
    private val cellEditorStateMs:Ms<CellEditorState>,
) : MouseOnWorksheetAction, ClickOnCell by clickOnCell {

    private val sc by scMs

    fun shiftClickSelectRange(cellAddress: CellAddress, wsState: WorksheetState?) {
        wsState?.also {
            var cursorState by wsState.cursorStateMs
            if (cellAddress != cursorState.mainCell) {
                cursorState = cursorState.setMainRange(RangeAddresses.from2Cells(cellAddress, cursorState.mainCell))
            }
            updateRangeSelectorText.updateRangeSelectorTextInCurrentCellEditor()
        }
    }

    override fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWsSt) {
        shiftClickSelectRange(cellAddress,sc.getWsState(cursorLoc))
    }

    override fun shiftClickSelectRange(cellAddress: CellAddress, cursorLoc: WbWs) {
        shiftClickSelectRange(cellAddress,sc.getWsState(cursorLoc))
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

    override fun startDragSelection(wbwsSt: WbWsSt, anchorCell: CellAddress) {
        clickOnCell(anchorCell, wbwsSt)
    }

    fun startDragSelection(wsState:WorksheetState?, mousePosition: Offset, offset: Offset) {
        wsState?.also {
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
    override fun startDragSelection(wbws: WbWsSt, mousePosition: Offset, offset: Offset) {
        startDragSelection(this.sc.getWsState(wbws),mousePosition, offset)
    }

    override fun startDragSelection(
        wbws: WbWs,
        mousePosition: Offset,
        offset: Offset,
    ) {
        startDragSelection(this.sc.getWsState(wbws),mousePosition, offset)
    }

    fun stopDragSelection(wsState: WorksheetState?){
        wsState?.also {
            val selectRect by wsState.selectRectStateMs
            wsState.selectRectStateMs.value = selectRect.hide().deactivate()
        }
    }

    override fun stopDragSelection(cursorLocation: WbWsSt) {
        stopDragSelection(sc.getWsState(cursorLocation))
    }

    override fun stopDragSelection(cursorLocation: WbWs) {
        stopDragSelection(sc.getWsState(cursorLocation))
    }

    fun makeMouseDragSelectionIfPossible(
        cursorStateMs:Ms<CursorState>?,
        currentCellMouseOn: CellAddress
    ) {
        cursorStateMs?.also {
            val cursorState by it
            val newRange = RangeAddresses.fromManyCells(listOf(currentCellMouseOn, cursorState.mainCell))
            val newCursorState = cursorState.setMainRange(newRange)
            it.value = newCursorState
            updateRangeSelectorText.updateRangeSelectorTextInCurrentCellEditor()
        }
    }

    override fun makeMouseDragSelectionIfPossible(cursorLocation: WbWsSt, currentCellMouseOn: CellAddress) {
        makeMouseDragSelectionIfPossible(sc.getCursorStateMs(cursorLocation),currentCellMouseOn)
    }

    override fun makeMouseDragSelectionIfPossible(
        cursorLocation: WbWs,
        currentCellMouseOn: CellAddress,
    ) {
        makeMouseDragSelectionIfPossible(sc.getCursorStateMs(cursorLocation),currentCellMouseOn)
    }

    fun makeMouseDragSelectionIfPossible(wsState: WorksheetState?, mousePosition: Offset, offset: Offset) {
        wsState?.also {
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
    override fun makeMouseDragSelectionIfPossible(cursorLocation: WbWsSt, mousePosition: Offset, offset: Offset) {
        makeMouseDragSelectionIfPossible(sc.getWsState(cursorLocation),mousePosition, offset)
    }

    override fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, mousePosition: Offset, offset: Offset) {
        makeMouseDragSelectionIfPossible(sc.getWsState(cursorLocation),mousePosition, offset)
    }

    private fun f(cellAddress: CellAddress, cursorLoc: WbWsSt) {
        sc.getWsState(cursorLoc) ?.also {wsState->
            val cursorState by wsState.cursorStateMs
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
                    wsState.cursorStateMs.value = newCursor
                } else {
                    wsState.cursorStateMs.value = cursorState.addFragCell(cellAddress)
                }
            }
        }
    }
    override fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWsSt) {
        val ce by cellEditorStateMs
        if (ce.isActiveAndAllowRangeSelector) {
            return
        }
        if (ce.isOpen && !ce.allowRangeSelector) {
            cellEditorStateMs.value = ce.close()
            f(cellAddress, cursorLoc)
            return
        }
        if (!ce.isOpen && !ce.allowRangeSelector) {
            f(cellAddress, cursorLoc)
        }
    }

    override fun ctrlClickSelectCell(cellAddress: CellAddress, cursorLoc: WbWs) {
        sc.getWbWsSt(cursorLoc)?.also {
            ctrlClickSelectCell(cellAddress, it)
        }
    }
}
