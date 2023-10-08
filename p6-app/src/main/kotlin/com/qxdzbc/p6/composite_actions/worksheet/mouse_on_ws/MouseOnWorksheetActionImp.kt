package com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.qxdzbc.p6.composite_actions.cell_editor.update_range_selector_text.RefreshRangeSelectorText
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.worksheet.make_cell_editor_display_text.GenerateCellEditorTextAction
import com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws.click_on_cell.ClickOnCellAction
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class,boundType=MouseOnWorksheetAction::class)
class MouseOnWorksheetActionImp @Inject constructor(
    private val scMs:StateContainer,
    private val clickOnCell: ClickOnCellAction,
    private val makeDisplayText: GenerateCellEditorTextAction,
    private val refreshRangeSelectorText: RefreshRangeSelectorText,
    private val cellEditorStateMs:Ms<CellEditorState>,
) : MouseOnWorksheetAction, ClickOnCellAction by clickOnCell {

    private val sc = scMs

    fun shiftClickSelectRange(cellAddress: CellAddress, wsState: WorksheetState?) {
        wsState?.also {
            var cursorState by wsState.cursorStateMs
            if (cellAddress != cursorState.mainCell) {
                cursorState = cursorState.setMainRange(RangeAddressUtils.rangeFor2Cells(cellAddress, cursorState.mainCell))
            }
            refreshRangeSelectorText.refreshRangeSelectorTextInCurrentCellEditor()
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

    fun startDragSelection(wsState:WorksheetState?, mousePosition: Offset) {
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
    override fun startDragSelection(wbws: WbWsSt, mousePosition: Offset) {
        startDragSelection(this.sc.getWsState(wbws),mousePosition)
    }

    override fun startDragSelection(
        wbws: WbWs,
        mousePosition: Offset,
    ) {
        startDragSelection(this.sc.getWsState(wbws),mousePosition)
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
            val newRange = RangeAddressUtils.rangeForMultiCells(listOf(currentCellMouseOn, cursorState.mainCell))
            val newCursorState = cursorState.setMainRange(newRange)
            it.value = newCursorState
            refreshRangeSelectorText.refreshRangeSelectorTextInCurrentCellEditor()
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

    fun makeMouseDragSelectionIfPossible(wsState: WorksheetState?, mousePosition: Offset) {
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
    override fun makeMouseDragSelectionIfPossible(cursorLocation: WbWsSt, mousePosition: Offset) {
        makeMouseDragSelectionIfPossible(sc.getWsState(cursorLocation),mousePosition)
    }

    override fun makeMouseDragSelectionIfPossible(cursorLocation: WbWs, mousePosition: Offset) {
        makeMouseDragSelectionIfPossible(sc.getWsState(cursorLocation),mousePosition)
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
