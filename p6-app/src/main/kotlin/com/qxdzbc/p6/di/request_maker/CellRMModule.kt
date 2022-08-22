package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.app.action.cell.CellRMImp
import com.qxdzbc.p6.app.action.cell.cell_update.rm.CellUpdateRM
import com.qxdzbc.p6.app.action.cell.cell_update.rm.CellUpdateRMImp
import com.qxdzbc.p6.app.action.cell.cell_update.rm.FakeCellUpdateRM
import com.qxdzbc.p6.di.P6Singleton
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
    @com.qxdzbc.p6.di.Fake
    fun FakeCellRequestMaker(cmk: FakeCellUpdateRM): CellUpdateRM
}
