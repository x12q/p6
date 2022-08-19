package com.emeraldblast.p6.app.action.worksheet.action2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.worksheet.click_on_cell.ClickOnCell
import com.emeraldblast.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject


class MouseOnWorksheetActionImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
    private val clickOnCell: ClickOnCell,
    private val makeDisplayText: MakeCellEditorDisplayText,
    private val updateRangeSelectorText: UpdateRangeSelectorText,
) : MouseOnWorksheetAction, ClickOnCell by clickOnCell {
    private val appState by appStateMs
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
                val cellRect = layout.boundInWindow
                if (cellRect.contains(selectRect.anchorPoint)) {
                    startDragSelection(wsState, cellAddress)
                    break
                }
            }
        }
    }

    override fun stopDragSelection(cursorLocation: WbWs) {
        appState.getWsState(cursorLocation)?.also {wsState->
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
        appState.getWsState(cursorLocation)?.also { wsState->
            val selectRect by wsState.selectRectStateMs
            if (selectRect.isActive) {
                wsState.selectRectStateMs.value = selectRect.setMovingPoint(mousePosition).show()
                val currentCellMouseOn = wsState.cellLayoutCoorMap.entries.firstOrNull { (_, layout) ->
                    val cellRect = layout.boundInWindow
                    cellRect.contains(mousePosition)
                }?.key
                if (currentCellMouseOn != null) {
                    makeMouseDragSelectionIfPossible(wsState, currentCellMouseOn)
                }
            }
        }
    }
}
