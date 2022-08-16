package com.emeraldblast.p6.ui.window.workbook_tab.bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.emeraldblast.p6.ui.window.move_to_wb.MoveToWbAction
import javax.inject.Inject


class WorkbookTabBarActionImp @Inject constructor(
    private val windowAction: WindowAction,
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val moveToWb: MoveToWbAction,
) : WorkbookTabBarAction,MoveToWbAction by moveToWb {
    private var appState by appStateMs

    override fun createNewWb(windowId:String) {
        windowAction.createNewWorkbook(windowId)
    }

    override fun close(wbKey: WorkbookKey,windowId:String) {
        windowAction.closeWorkbook(wbKey,windowId)
    }
}
