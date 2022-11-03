package com.qxdzbc.p6.di.applier

import com.qxdzbc.p6.app.action.applier.*
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module(
    includes = [
        WorksheetApplierModule::class,
        WindowApplierModule::class,
        CellApplierModule::class,
        WorkbookApplierModule::class,
        RangeApplierModule::class,
        ScriptApplierModule::class,
    ]
)
interface ApplierModule {
//    @Binds
//    @P6Singleton
//    fun TemplateResponseApplier(i: ErrorApplierImp): ErrorApplier
//
//    @Binds
//    @P6Singleton
//    fun WorkbookUpdateCommonApplier(i: WorkbookUpdateCommonApplierImp): WorkbookUpdateCommonApplier
//
//    @Binds
//    @P6Singleton
//    fun BaseResApplier2(i: BaseApplierImp):BaseApplier
}
