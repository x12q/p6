package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookAction
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookActionImp
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplier
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplier
import com.qxdzbc.p6.app.action.app.close_wb.applier.CloseWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookAction
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookActionImp
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookAction
import com.qxdzbc.p6.app.action.app.get_wb.GetWorkbookActionImp
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookAction
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookActionImp
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplier
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplier
import com.qxdzbc.p6.app.action.app.load_wb.applier.LoadWorkbookInternalApplierImp
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplier
import com.qxdzbc.p6.app.action.app.restart_kernel.applier.RestartKernelApplierImp
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookAction
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookActionImp
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookApplier
import com.qxdzbc.p6.app.action.app.save_wb.applier.SaveWorkbookApplierImp
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookAction
import com.qxdzbc.p6.app.action.app.set_active_wb.SetActiveWorkbookActionImp
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowAction
import com.qxdzbc.p6.app.action.app.set_active_wd.SetActiveWindowActionImp
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyAction
import com.qxdzbc.p6.app.action.app.set_wbkey.ReplaceWorkbookKeyActionImp
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.set_active_ws.applier.SetActiveWorksheetApplierImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface AppActionModule {
//    @Binds
//    @P6Singleton
//    fun SetActiveWindowAction(i: SetActiveWindowActionImp):SetActiveWindowAction
//
//    @Binds
//    @P6Singleton
//    fun GetWorkbookAction(i: GetWorkbookActionImp):GetWorkbookAction
//
//    @Binds
//    @P6Singleton
//    fun LoadWorkbookAction(i: LoadWorkbookActionImp):LoadWorkbookAction
//
//    @Binds
//    @P6Singleton
//    fun SaveWorkbookAction(i: SaveWorkbookActionImp): SaveWorkbookAction
//
//    @Binds
//    @P6Singleton
//    fun SetActiveWorkbookAction(i: SetActiveWorkbookActionImp):SetActiveWorkbookAction
//
//    @Binds
//    @P6Singleton
//    fun NewWorkbookAction(i:CreateNewWorkbookActionImp): CreateNewWorkbookAction
//
//    @Binds
//    @P6Singleton
//    fun CloseWbAction(i:CloseWorkbookActionImp): CloseWorkbookAction
//
//
//    @Binds
//    @P6Singleton
//    fun CreateNewWorkbookApplier(i: CreateNewWorkbookApplierImp): CreateNewWorkbookApplier
//
//
//    @Binds
//    @P6Singleton
//    fun SaveWorkbookApplier(i: SaveWorkbookApplierImp): SaveWorkbookApplier
//
//    @Binds
//    @P6Singleton
//    fun SetActiveWorksheetApplier(i: SetActiveWorksheetApplierImp): SetActiveWorksheetApplier
//
//    @Binds
//    @P6Singleton
//    fun LoadWorkbookInternalApplier(i: LoadWorkbookInternalApplierImp): LoadWorkbookInternalApplier
//
//    @Binds
//    @P6Singleton
//    fun LoadWorkbookApplier(i: LoadWorkbookApplierImp): LoadWorkbookApplier
//
//    @Binds
//    @P6Singleton
//    fun CloseWorkbookInternalApplier(i: CloseWorkbookInternalApplierImp): CloseWorkbookInternalApplier
//
//    @Binds
//    @P6Singleton
//    fun CloseWorkbookApplier(i: CloseWorkbookApplierImp): CloseWorkbookApplier
}
