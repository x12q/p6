package com.emeraldblast.p6.di.action

import com.emeraldblast.p6.app.action.app.close_wb.CloseWbAction
import com.emeraldblast.p6.app.action.app.close_wb.CloseWbActionImp
import com.emeraldblast.p6.app.action.app.create_new_wb.NewWorkbookAction
import com.emeraldblast.p6.app.action.app.create_new_wb.NewWorkbookActionImp
import com.emeraldblast.p6.app.action.app.set_wbkey.SetWorkbookKeyAction
import com.emeraldblast.p6.app.action.app.set_wbkey.SetWorkbookKeyActionImp
import com.emeraldblast.p6.app.action.app.set_wbkey.applier.SetWbKeyApplier
import com.emeraldblast.p6.app.action.app.set_wbkey.applier.SetWbKeyApplierImp
import com.emeraldblast.p6.app.action.app.set_wbkey.rm.SetWbKeyRM
import com.emeraldblast.p6.app.action.app.set_wbkey.rm.SetWbKeyRMImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface AppActionModule {
    @Binds
    @P6Singleton
    fun NewWorkbookAction(i:NewWorkbookActionImp): NewWorkbookAction

    @Binds
    @P6Singleton
    fun CloseWbAction(i:CloseWbActionImp): CloseWbAction

    @Binds
    @P6Singleton
    fun SetWbKeyAction(i: SetWorkbookKeyActionImp): SetWorkbookKeyAction

    @Binds
    @P6Singleton
    fun SetWbKeyApplier(i: SetWbKeyApplierImp): SetWbKeyApplier

    @Binds
    @P6Singleton
    fun SetWbKeyRM(i: SetWbKeyRMImp): SetWbKeyRM
}
