package com.qxdzbc.p6.di.state

import com.qxdzbc.p6.di.state.wb.WorkbookStateModule
import com.qxdzbc.p6.di.state.window.WindowStateModule
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule

@dagger.Module(
    includes = [
        WorksheetStateModule::class,
        WorkbookStateModule::class,
        WindowStateModule::class,
    ]
)
interface StateModule {
}

