package com.emeraldblast.p6.di.state

import com.emeraldblast.p6.di.state.wb.WorkbookStateModule
import com.emeraldblast.p6.di.state.window.WindowStateModule
import com.emeraldblast.p6.di.state.ws.WorksheetStateModule

@dagger.Module(
    includes = [
        WorksheetStateModule::class,
        WorkbookStateModule::class,
        WindowStateModule::class,
    ]
)
interface StateModule {
}

