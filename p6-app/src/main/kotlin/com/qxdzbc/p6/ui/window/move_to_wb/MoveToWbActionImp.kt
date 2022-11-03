package com.qxdzbc.p6.ui.window.move_to_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class MoveToWbActionImp @Inject constructor(
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val appStateMs: Ms<AppState>,
    @StateContainerSt private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
) : MoveToWbAction {

    private var appState by appStateMs
    private val stateCont by stateContSt

    override fun moveToWorkbook(wbKey: WorkbookKey) {
        restoreWindowFocusState.setFocusStateConsideringRangeSelector(wbKey)
        var cellEditorState by appState.cellEditorStateMs
        if(cellEditorState.isActive && cellEditorState.allowRangeSelector){
            val newRangeCursorMs = appState.getActiveCursorMs(wbKey)
            if(newRangeCursorMs!=null){
                cellEditorState = cellEditorState.setRangeSelectorCursorId(newRangeCursorMs.value.idMs)
            }else{
                // x: this happens when the target workbook is empty
                cellEditorState = cellEditorState.clearAllText().close()
                restoreWindowFocusState.restoreAllWsFocusIfAllow()
            }
        }

        setActiveWb(wbKey)
    }

    override fun setActiveWb(wbKey: WorkbookKey) {
        val windowState = stateCont.getWindowStateByWbKey(wbKey)
        windowState?.also {wds->
            appState.activeWindowPointer = appState.activeWindowPointer.pointTo(wds.id)
            val wbkMs = stateCont.getWbKeyMs(wbKey)
            wbkMs?.also {
                wds.activeWbPointer = wds.activeWbPointer.pointTo(it)
            }
        }
    }
}
