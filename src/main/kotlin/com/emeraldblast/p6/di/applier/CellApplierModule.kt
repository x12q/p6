package com.emeraldblast.p6.di.applier

import com.emeraldblast.p6.app.action.cell.cell_update.applier.CellUpdateApplier
import com.emeraldblast.p6.app.action.cell.cell_update.applier.CellUpdateApplierImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface CellApplierModule {
    @Binds
    @P6Singleton
    fun CellEventApplier(i: CellUpdateApplierImp): CellUpdateApplier
}
