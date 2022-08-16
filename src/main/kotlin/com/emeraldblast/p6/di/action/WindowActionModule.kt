package com.emeraldblast.p6.di.action

import com.emeraldblast.p6.di.P6Singleton
import com.emeraldblast.p6.app.action.window.WindowAction
import com.emeraldblast.p6.app.action.window.WindowActionImp
import com.emeraldblast.p6.ui.window.menu.action.CodeMenuAction
import com.emeraldblast.p6.ui.window.menu.action.CodeMenuActionImp
import com.emeraldblast.p6.ui.window.menu.action.FileMenuAction
import com.emeraldblast.p6.ui.window.menu.action.FileMenuActionImp
import com.emeraldblast.p6.ui.window.move_to_wb.MoveToWbAction
import com.emeraldblast.p6.ui.window.move_to_wb.MoveToWbActionImp
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarAction
import com.emeraldblast.p6.ui.window.workbook_tab.bar.WorkbookTabBarActionImp
import dagger.Binds

@dagger.Module
interface WindowActionModule {
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
