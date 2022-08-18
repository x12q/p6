package com.emeraldblast.p6.app.action.workbook.click_on_ws_tab_item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.emeraldblast.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.emeraldblast.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class SwitchWorksheetActionImp @Inject constructor(
    val setActiveWorksheetAction: SetActiveWorksheetAction,
    val restoreWindowFocusAction: RestoreWindowFocusState,
    @AppStateMs
    val appStateMs: Ms<AppState>
) : SwitchWorksheetAction {
    var appState by appStateMs
    override fun switchToWorksheet(request: SetActiveWorksheetRequest): RseNav<SetActiveWorksheetResponse2> {
        restoreWindowFocusAction.setFocusStateConsideringRangeSelector(request.wbKey)
        var cellEditorState by appState.cellEditorStateMs
        if(cellEditorState.isActive && cellEditorState.allowRangeSelector){
            appState.getCursorStateMs(request)?.also {
                cellEditorState = cellEditorState.setRangeSelectorCursorId(it.value.idMs)
            }
        }
        val rt = setActiveWorksheetAction.setActiveWs(request)
        return rt
    }
}
