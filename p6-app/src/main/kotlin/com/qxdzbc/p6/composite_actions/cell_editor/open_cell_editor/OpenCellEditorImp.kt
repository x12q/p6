package com.qxdzbc.p6.composite_actions.cell_editor.open_cell_editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.CellErrors
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class OpenCellEditorImp @Inject constructor(
    val stateCont:StateContainer,
    private val docCont: DocumentContainer,
    val appState:AppState,
    val errorRouter: ErrorRouter,
) : OpenCellEditorAction {

    val sc  = stateCont

    override fun openCellEditor(wbws: WbWs) {
        val ws = docCont.getWs(wbws)
        val cursorStateMs = sc.getCursorStateMs(wbws)
        if (ws != null && cursorStateMs != null) {
            openCellEditor(ws, cursorStateMs)
        }
    }

    override fun openCellEditor(wbwsSt: WbWsSt) {
        val ws = docCont.getWs(wbwsSt)
        val cursorStateMs = sc.getCursorStateMs(wbwsSt)
        if (ws != null && cursorStateMs != null) {
            openCellEditor(ws, cursorStateMs)
        }
    }

    override fun openCellEditorOnActiveWs() {
        val activeSheetState = sc.getActiveWindowState()?.activeWbState?.activeSheetState
        activeSheetState?.also {
            openCellEditor(it)
        }
    }

    private fun openCellEditor(ws: Worksheet, cursorStateMs: Ms<CursorState>) {
        val cursorState by cursorStateMs
        var cellEditorState by appState.cellEditorStateMs
        if (!cellEditorState.isOpen) {
            val cursorMainCell: Rse<com.qxdzbc.p6.document_data_layer.cell.Cell> = ws.getCellOrDefaultRs(cursorState.mainCell)
            val windowState = sc.getWindowStateMsByWbKey(ws.wbKey)
            val windowId = windowState?.id
            val fcsMs = windowState?.focusStateMs
            cursorMainCell
                .onSuccess { cell ->
                    if (cell.isEditable) {
                        if (fcsMs != null) {
                            fcsMs.value = fcsMs.value.focusOnEditor()
                        }
                        val cellText = cell.editableText(cursorState.wbKey, cursorState.wsName)
                        cellEditorState = cellEditorState
                            .setCurrentText(cellText)
                            .setTargetCell(cursorState.mainCell)
                            .open(cursorState.idMs)
                    } else {
                        errorRouter.publishToWindow(CellErrors.NotEditable.report(cell.address), windowId)
                    }
                }
                .onFailure {
                    errorRouter.publishToWindow(it, windowId)
                }
        }
    }
}
