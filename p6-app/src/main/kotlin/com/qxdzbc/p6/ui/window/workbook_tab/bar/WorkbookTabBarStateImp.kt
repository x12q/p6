package com.qxdzbc.p6.ui.window.workbook_tab.bar

import com.qxdzbc.p6.ui.window.state.WindowState
import com.qxdzbc.p6.ui.window.workbook_tab.tab.WorkbookTabState
import com.qxdzbc.p6.ui.window.workbook_tab.tab.WorkbookTabStateImp

class WorkbookTabBarStateImp(
    val windowState: WindowState
) : WorkbookTabBarState {
    override val windowId: String
        get() = windowState.id
    override val tabStateList: List<WorkbookTabState>
        get() {
            return windowState.wbStateMsList.map {
                WorkbookTabStateImp(
                    wbKey =it.wbKey,
                    isSelected =it.wbKey == windowState.activeWbPointer.wbKey,
                    needSave = it.needSave
                )
            }
        }
    override val size: Int
        get() = tabStateList.size
}
