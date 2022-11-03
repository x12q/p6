package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.P6ResponseLegalityChecker
import com.qxdzbc.p6.app.action.P6ResponseLegalityCheckerImp
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellActionImp
import dagger.Binds

@dagger.Module(
    includes = [
        AppActionModule::class,
        RpcActionModule::class,
        WorkbookActionModule::class,
        WorksheetActionModule::class,
        RangeActionModule::class,
        WindowActionModule::class,
        CellEditorActionModule::class,
        CellActionModule::class,
    ]
)
interface ActionModule {

}
