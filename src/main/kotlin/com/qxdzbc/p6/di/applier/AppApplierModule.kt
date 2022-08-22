package com.qxdzbc.p6.di.applier

import com.qxdzbc.p6.app.action.app.AppApplier
import com.qxdzbc.p6.app.action.app.AppApplierImp
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplier
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplier
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookInternalApplier
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplier
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplier
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplier
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplierImp
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookApplier
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookInternalApplier
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplierImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface AppApplierModule {

    @Binds
    @P6Singleton
    fun RestartKernelApplier(i: RestartKernelApplierImp): RestartKernelApplier

    @Binds
    @P6Singleton
    fun AppEventApplier(i: AppApplierImp): AppApplier

    @Binds
    @P6Singleton
    fun CreateNewWorkbookInternalApplier(i: CreateNewWorkbookInternalApplierImp): CreateNewWorkbookInternalApplier

    @Binds
    @P6Singleton
    fun CreateNewWorkbookApplier(i: CreateNewWorkbookApplierImp): CreateNewWorkbookApplier

    @Binds
    @P6Singleton
    fun SaveWorkbookInternalApplier(i: SaveWorkbookInternalApplierImp): SaveWorkbookInternalApplier

    @Binds
    @P6Singleton
    fun SaveWorkbookApplier(i: SaveWorkbookApplierImp): SaveWorkbookApplier

    @Binds
    @P6Singleton
    fun SetActiveWorksheetApplier(i: SetActiveWorksheetApplierImp): SetActiveWorksheetApplier

    @Binds
    @P6Singleton
    fun LoadWorkbookInternalApplier(i: LoadWorkbookInternalApplierImp): LoadWorkbookInternalApplier

    @Binds
    @P6Singleton
    fun LoadWorkbookApplier(i: LoadWorkbookApplierImp): LoadWorkbookApplier

    @Binds
    @P6Singleton
    fun CloseWorkbookInternalApplier(i: CloseWorkbookInternalApplierImp): CloseWorkbookInternalApplier

    @Binds
    @P6Singleton
    fun CloseWorkbookApplier(i: CloseWorkbookApplierImp): CloseWorkbookApplier
}
