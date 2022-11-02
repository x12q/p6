package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.app.AppRM
import com.qxdzbc.p6.app.action.app.AppRMImp
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.qxdzbc.p6.app.action.app.close_wb.rm.CloseWorkbookRMImp
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.CreateNewWbRMImp
import com.qxdzbc.p6.app.action.app.create_new_wb.rm.FakeNewWbRM
import com.qxdzbc.p6.app.action.app.load_wb.rm.LoadWorkbookRM
import com.qxdzbc.p6.app.action.app.load_wb.rm.LoadWorkbookRMImp
import com.qxdzbc.p6.app.action.app.restart_kernel.rm.RestartKernelRM
import com.qxdzbc.p6.app.action.app.restart_kernel.rm.RestartKernelRMImp
import com.qxdzbc.p6.app.action.app.save_wb.rm.SaveWorkbookRM
import com.qxdzbc.p6.app.action.app.save_wb.rm.SaveWorkbookRMImp
import com.qxdzbc.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRM
import com.qxdzbc.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRMImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds


@dagger.Module
interface AppRMModule {
    @Binds
    @P6Singleton
    fun RestartKernelRM(i: RestartKernelRMImp): RestartKernelRM

    @Binds
    @P6Singleton
    fun CreateNewWbRM(i: CreateNewWbRMImp): CreateNewWbRM

    @Binds
    @P6Singleton
    fun CloseWorkbookRM(i: CloseWorkbookRMImp): CloseWorkbookRM

    @Binds
    @P6Singleton
    fun SetActiveWorksheetRM(i: SetActiveWorksheetRMImp): SetActiveWorksheetRM

    @Binds
    @P6Singleton
    fun AppRM(mk: AppRMImp): AppRM


    @Binds
    @P6Singleton
    @com.qxdzbc.p6.di.Fake
    fun FakeMakeCreateNewWbRequest(i: FakeNewWbRM): CreateNewWbRM

    @Binds
    @P6Singleton
    fun SaveWorkbookRM(i: SaveWorkbookRMImp): SaveWorkbookRM

    @Binds
    @P6Singleton
    fun LoadWorkbookRM(i: LoadWorkbookRMImp): LoadWorkbookRM

}
