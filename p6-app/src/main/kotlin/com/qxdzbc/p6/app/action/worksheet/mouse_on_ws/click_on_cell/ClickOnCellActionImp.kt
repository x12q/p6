package com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.click_on_cell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.cell_editor.update_range_selector_text.UpdateRangeSelectorText
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.document.cell.address.CellAddress


import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.app.action.cell_editor.run_formula.RunFormulaOrSaveValueToCellAction
import com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor.OnCursorChangeEventReactor
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ClickOnCellActionImp @Inject constructor(
    private val appStateMs: Ms<AppState>,
    private val stateContSt: St<@JvmSuppressWildcards SubAppStateContainer>,
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val updateRangeSelectorText: UpdateRangeSelectorText,
    private val runFormulaAction: RunFormulaOrSaveValueToCellAction,
    private val onCursorChangeEventReactor: OnCursorChangeEventReactor,
) : ClickOnCellAction {

    private val stateCont by stateContSt
    private var appState by appStateMs

    fun clickOnCell(cellAddress: CellAddress, cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            val editorStateMs = appState.cellEditorStateMs
            val editorState by editorStateMs
            val cursorState by cursorStateMs
            val cellEditorState by cursorState.cellEditorStateMs
            restoreWindowFocusState.setFocusStateConsideringRangeSelector(cursorState.wbKey)
            cursorStateMs.value = cursorState
                .setMainCell(cellAddress)
                .removeMainRange()
                .removeAllSelectedFragRange()
                .removeAllFragmentedCells()
            val rangeSelectorIsActivated:Boolean = cellEditorState.isOpen && cellEditorState.rangeSelectorAllowState.isAllow()
            if (rangeSelectorIsActivated) {
                editorStateMs.value = editorState.setRangeSelectorCursorId(cursorState.idMs)
                updateRangeSelectorText.updateRangeSelectorTextInCurrentCellEditor()
            }else{
                runFormulaAction.runFormulaOrSaveValueToCell()
                editorStateMs.value=editorState.close()
            }
            onCursorChangeEventReactor.onCursorChanged(cursorStateMs.value.id)
        }
    }

    /**
     * when click on a cell, remove all previous selection, then assign the clicked cell as the new main cell
     */
    override fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWs) {
        clickOnCell(cellAddress,stateCont.getCursorStateMs(cursorLocation))
    }

    override fun clickOnCell(cellAddress: CellAddress, cursorLocation: WbWsSt) {
        clickOnCell(cellAddress,stateCont.getCursorStateMs(cursorLocation))
    }
}
