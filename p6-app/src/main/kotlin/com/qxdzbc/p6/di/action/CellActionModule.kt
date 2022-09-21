package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.cell.cell_multi_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.cell.cell_multi_update.MultiCellUpdateActionImp
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellAction
import com.qxdzbc.p6.app.action.cell.copy_cell.CopyCellActionImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds


@dagger.Module
interface CellActionModule {
    @Binds
    @P6Singleton
    fun CopyCellAction(i:CopyCellActionImp): CopyCellAction

    @Binds
    @P6Singleton
    fun MultiCellUpdateAction(i: MultiCellUpdateActionImp):MultiCellUpdateAction
}
