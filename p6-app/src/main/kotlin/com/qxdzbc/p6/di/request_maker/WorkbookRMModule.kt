package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.workbook.add_ws.rm.CreateNewWorksheetRM
import com.qxdzbc.p6.app.action.workbook.add_ws.rm.CreateNewWorksheetRMImp
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRM
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRMImp
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.NewWorksheetRM
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.NewWorksheetRMImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WorkbookRMModule {
    @Binds
    @P6Singleton
    fun AddWorksheetRM(i: CreateNewWorksheetRMImp): CreateNewWorksheetRM

    @Binds
    @P6Singleton

    fun DeleteWorksheetRMLocal(i: DeleteWorksheetRMImp): DeleteWorksheetRM

    @Binds
    @P6Singleton

    fun NewWorksheetRMLocal(i: NewWorksheetRMImp): NewWorksheetRM

//    @Binds
//    @P6Singleton
//    fun WorkbookRequestMaker(mk: WorkbookRMImp): WorkbookRM
}
