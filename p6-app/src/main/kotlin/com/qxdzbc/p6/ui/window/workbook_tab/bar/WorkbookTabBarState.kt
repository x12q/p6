package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.ui.window.workbook_tab.tab.WorkbookTabState

interface WorkbookTabBarState : WithSize {
    val windowId:String
    val tabStateList: List<WorkbookTabState>
}
