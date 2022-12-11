package com.qxdzbc.p6.di.state

import com.qxdzbc.p6.di.state.app_state.CellFormatTableModule
import com.qxdzbc.p6.di.state.wb.WorkbookStateModule
import com.qxdzbc.p6.di.state.window.WindowStateModule
import com.qxdzbc.p6.di.state.ws.WorksheetStateModule
import com.qxdzbc.p6.di.state.ws.cursor.CursorStateModule

@dagger.Module(
    includes = [
        WorksheetStateModule::class,
        WorkbookStateModule::class,
        WindowStateModule::class,
        CursorStateModule::class,
            CellFormatTableModule::class,
    ]
)
interface StateModule {
}

