package com.emeraldblast.p6.ui.window.workbook_tab.bar

import com.emeraldblast.p6.app.common.utils.WithSize
import com.emeraldblast.p6.ui.window.workbook_tab.tab.WorkbookTabState

interface WorkbookTabBarState : WithSize {
    val windowId:String
    val tabStateList: List<WorkbookTabState>
}
