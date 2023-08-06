package com.qxdzbc.p6.di.state

import com.qxdzbc.p6.di.XModule
import com.qxdzbc.p6.di.state.app_state.AppStateModule
import com.qxdzbc.p6.ui.document.workbook.di.WorkbookStateModule
import com.qxdzbc.p6.ui.document.worksheet.di.CursorStateModule

import dagger.Module
@Module(
    includes = [
        AppStateModule::class,
        WorkbookStateModule::class,
        CursorStateModule::class,
        XModule::class,
    ]
)
interface StateModule {
}

