package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.cell.CellRM
import com.emeraldblast.p6.app.action.cell.CellRMImp
import com.emeraldblast.p6.app.action.cell.cell_update.rm.CellUpdateRM
import com.emeraldblast.p6.app.action.cell.cell_update.rm.CellUpdateRMImp
import com.emeraldblast.p6.app.action.cell.cell_update.rm.FakeCellUpdateRM
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface CellRMModule {

    @Binds
    @P6Singleton
    
    fun CellUpdateRMLocalImp(i: CellUpdateRMImp): CellUpdateRM

    @Binds
    @P6Singleton
    fun CellRequestMaker(cmk: CellRMImp): CellRM

    @Binds
    @P6Singleton
    @com.emeraldblast.p6.di.Fake
    fun FakeCellRequestMaker(cmk: FakeCellUpdateRM): CellUpdateRM
}
