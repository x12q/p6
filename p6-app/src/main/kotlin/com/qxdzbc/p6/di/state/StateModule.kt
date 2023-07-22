package com.qxdzbc.p6.di.state

import com.qxdzbc.p6.di.state.app_state.AppStateModule
import com.qxdzbc.p6.di.state.wb.WorkbookStateModule
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule
import com.qxdzbc.p6.di.state.ws.cursor.CursorStateModule

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

