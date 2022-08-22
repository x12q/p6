package com.qxdzbc.p6.ui.window.move_to_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import javax.inject.Inject

class MoveToWbActionImp @Inject constructor(
    private val restoreWindowFocusState: RestoreWindowFocusState,
    @AppStateMs private val appStateMs: Ms<AppState>,
) : MoveToWbAction {
    private var appState by appStateMs
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
        appState.queryStateByWorkbookKey(wbKey).ifOk {
            val activeWbPointer = it.windowStateMs.value.activeWorkbookPointerMs
            activeWbPointer.value = activeWbPointer.value.pointTo(wbKey)
        }
    }
}
