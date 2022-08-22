package com.qxdzbc.p6.di.applier

import com.qxdzbc.p6.app.action.cell.cell_update.applier.CellUpdateApplier
import com.qxdzbc.p6.app.action.cell.cell_update.applier.CellUpdateApplierImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface CellApplierModule {
    @Binds
    @P6Singleton
    fun CellEventApplier(i: CellUpdateApplierImp): CellUpdateApplier
}
