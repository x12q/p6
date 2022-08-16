package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.workbook.add_ws.rm.AddWorksheetRM
import com.emeraldblast.p6.app.action.workbook.add_ws.rm.AddWorksheetRMImp
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRM
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRMImp
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.NewWorksheetRM
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.NewWorksheetRMImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WorkbookRMModule {
    @Binds
    @P6Singleton
    fun AddWorksheetRM(i: AddWorksheetRMImp): AddWorksheetRM

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
