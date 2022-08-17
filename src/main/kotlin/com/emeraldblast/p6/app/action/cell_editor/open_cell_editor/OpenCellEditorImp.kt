package com.emeraldblast.p6.app.action.cell_editor.open_cell_editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.document.cell.CellErrors
import com.emeraldblast.p6.app.document.cell.d.Cell
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.di.state.app_state.DocumentContainerSt
import com.emeraldblast.p6.di.state.app_state.StateContainerSt
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.DocumentContainer
import com.emeraldblast.p6.ui.app.state.StateContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject

class OpenCellEditorImp @Inject constructor(
    @StateContainerSt
    val stateContMs:St<@JvmSuppressWildcards StateContainer>,
    @DocumentContainerSt
    val docContSt:St<@JvmSuppressWildcards DocumentContainer>,
    @AppStateMs
    val appStateMs:Ms<AppState>,
    val errorRouter:ErrorRouter,
) : OpenCellEditorAction {
    val appState by appStateMs
    val docCont by docContSt
    val stateCont by stateContMs
    override fun openCellEditor(wsId: WithWbWs) {
        val ws = docCont.getWorksheet(wsId)
        val cursorStateMs = stateCont.getCursorStateMs(wsId)
        if(ws!=null && cursorStateMs!=null){
            val cursorState by cursorStateMs
            var cellEditorState by appState.cellEditorStateMs
            if(!cellEditorState.isActive){
                val cursorMainCell:Rse<Cell> = ws.getCellOrDefaultRs(cursorState.mainCell)
                val windowStateMs = appState.getWindowStateMsByWbKey(wsId.wbKey)
                val windowId = windowStateMs?.value?.id
                val fcsMs = windowStateMs?.value?.focusStateMs
                cursorMainCell.onSuccess { cell->
                    if (cell.isEditable) {
                        if(fcsMs!=null){
                            fcsMs.value =fcsMs.value.focusOnEditor()
                        }
                        val cellText = cell.content.editableContent
                        cellEditorState = cellEditorState
                            .setCurrentText(cellText)
                            .setEditTarget(cursorState.mainCell)
                            .open(cursorState.idMs)
                    } else {
                        errorRouter.toWindow(CellErrors.NotEditable.report(cell.address), windowId)
                    }
                }.onFailure {
                    errorRouter.toWindow(it, windowId)
                }
            }
        }
    }
}
