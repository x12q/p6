package com.qxdzbc.p6.di.state

import com.qxdzbc.p6.di.state.app_state.AppStateModule
import com.qxdzbc.p6.ui.document.workbook.di.WorkbookStateModule
import com.qxdzbc.p6.ui.document.worksheet.di.WorksheetStateModule
import com.qxdzbc.p6.ui.document.worksheet.di.CursorStateModule

import dagger.Module
@Module(
    includes = [
        AppStateModule::class,
        WorksheetStateModule::class,
        WorkbookStateModule::class,
        CursorStateModule::class,
    ]
)
interface StateModule {
}

