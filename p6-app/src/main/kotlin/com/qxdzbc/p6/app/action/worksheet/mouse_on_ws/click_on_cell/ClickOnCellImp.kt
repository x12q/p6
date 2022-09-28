package com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorText
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.SubAppStateContainerSt
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import javax.inject.Inject

class ClickOnCellImp @Inject constructor(
    @AppStateMs
    private val appStateMs: Ms<AppState>,
    @SubAppStateContainerSt
    private val stateContSt: St<@JvmSuppressWildcards SubAppStateContainer>,
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val makeDisplayText: MakeCellEditorDisplayTextAction,
    private val updateRangeSelectorText: UpdateRangeSelectorText,
) : ClickOnCell {
    private val stateCont by stateContSt
    private var appState by appStateMs

    /**
     * when click on a cell, remove all previous selection, then assign the clicked cell as the new main cell
     */
    override fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWs) {

        stateCont.getCursorStateMs(cursorLocation)?.also { cursorStateMs ->
            val cursorState by cursorStateMs
            val cellEditorState by cursorState.cellEditorStateMs
            restoreWindowFocusState.setFocusStateConsideringRangeSelector(cursorState.wbKey)
            val c1 = cellEditorState.isActive && !cellEditorState.allowRangeSelector
            if (c1) {
                return
            } else {
                cursorStateMs.value = cursorState
                    .setMainCell(cellAddress)
                    .removeMainRange()
                    .removeAllSelectedFragRange()
                    .removeAllFragmentedCells()
            }
            val editorStateMs = appState.cellEditorStateMs
            val editorState by editorStateMs
            editorStateMs.value = editorState.setRangeSelectorCursorId(cursorState.idMs)
            updateRangeSelectorText.updateRangeSelectorText()
        }
    }
}
