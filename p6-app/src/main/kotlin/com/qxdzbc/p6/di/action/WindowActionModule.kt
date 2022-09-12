package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.app.action.window.WindowAction
import com.qxdzbc.p6.app.action.window.WindowActionImp
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbAction
import com.qxdzbc.p6.app.action.window.pick_active_wb.PickDefaultActiveWbActionImp
import com.qxdzbc.p6.ui.window.menu.action.CodeMenuAction
import com.qxdzbc.p6.ui.window.menu.action.CodeMenuActionImp
import com.qxdzbc.p6.ui.window.menu.action.FileMenuAction
import com.qxdzbc.p6.ui.window.menu.action.FileMenuActionImp
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbAction
import com.qxdzbc.p6.ui.window.move_to_wb.MoveToWbActionImp
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarActionImp
import dagger.Binds

@dagger.Module
interface WindowActionModule {
    @Binds
    @P6Singleton
    fun PickDefaultActiveWbAction(i:PickDefaultActiveWbActionImp): PickDefaultActiveWbAction

    @Binds
    @P6Singleton
    fun MoveToWbAction(i: MoveToWbActionImp):MoveToWbAction

    @Binds
    @P6Singleton
    fun WindowAction(i: WindowActionImp): WindowAction
    @Binds
    @P6Singleton
    fun FileMenuAction(i:FileMenuActionImp): FileMenuAction

    @Binds
    @P6Singleton
    fun CodeMenuAction(i:CodeMenuActionImp): CodeMenuAction
    @Binds
    @P6Singleton
    fun WorkbookTabBarAction(i: WorkbookTabBarActionImp):WorkbookTabBarAction
}
