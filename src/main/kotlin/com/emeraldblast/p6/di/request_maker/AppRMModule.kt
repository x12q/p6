package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.app.AppRM
import com.emeraldblast.p6.app.action.app.AppRMImp
import com.emeraldblast.p6.app.action.app.close_wb.rm.CloseWorkbookRM
import com.emeraldblast.p6.app.action.app.close_wb.rm.CloseWorkbookRMImp
import com.emeraldblast.p6.app.action.app.create_new_wb.rm.CreateNewWbRM
import com.emeraldblast.p6.app.action.app.create_new_wb.rm.CreateNewWbRMImp
import com.emeraldblast.p6.app.action.app.create_new_wb.rm.FakeNewWbRM
import com.emeraldblast.p6.app.action.app.load_wb.rm.LoadWorkbookRM
import com.emeraldblast.p6.app.action.app.load_wb.rm.LoadWorkbookRMImp
import com.emeraldblast.p6.app.action.app.restart_kernel.rm.RestartKernelRM
import com.emeraldblast.p6.app.action.app.restart_kernel.rm.RestartKernelRMImp
import com.emeraldblast.p6.app.action.app.save_wb.rm.SaveWorkbookRM
import com.emeraldblast.p6.app.action.app.save_wb.rm.SaveWorkbookRMImp
import com.emeraldblast.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRM
import com.emeraldblast.p6.app.action.workbook.set_active_ws.rm.SetActiveWorksheetRMImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds


@dagger.Module
interface AppRMModule {
    @Binds
    @P6Singleton

    fun RestartKernelRMImp(i: RestartKernelRMImp): RestartKernelRM

    @Binds
    @P6Singleton

    fun MakeCreateNewWbRequestLocal(i: CreateNewWbRMImp): CreateNewWbRM

    @Binds
    @P6Singleton

    fun MakeCloseWorkbookRequestLocal(i: CloseWorkbookRMImp): CloseWorkbookRM

    @Binds
    @P6Singleton

    fun SetActiveWorksheetRMLocal(i: SetActiveWorksheetRMImp): SetActiveWorksheetRM

    @Binds
    @P6Singleton
    fun AppRequestMaker(mk: AppRMImp): AppRM


    @Binds
    @P6Singleton
    @com.emeraldblast.p6.di.Fake
    fun FakeMakeCreateNewWbRequest(i: FakeNewWbRM): CreateNewWbRM

    @Binds
    @P6Singleton

    fun SaveWorkbookRMLocal(i: SaveWorkbookRMImp): SaveWorkbookRM

    @Binds
    @P6Singleton

    fun LoadWorkbookRMLocal(i: LoadWorkbookRMImp): LoadWorkbookRM

}
