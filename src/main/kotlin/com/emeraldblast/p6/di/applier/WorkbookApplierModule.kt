package com.emeraldblast.p6.di.applier


import com.emeraldblast.p6.app.action.workbook.add_ws.applier.AddWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.add_ws.applier.AddWorksheetApplierImp
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplierImp
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetInternalApplier
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetInternalApplierImp
import com.emeraldblast.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplierImp
import com.emeraldblast.p6.app.action.workbook.new_worksheet.applier.NewWorksheetInternalApplier
import com.emeraldblast.p6.app.action.workbook.new_worksheet.applier.NewWorksheetInternalApplierImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WorkbookApplierModule {
    @Binds
    @P6Singleton
    fun AddWsApplier(i: AddWorksheetApplierImp): AddWorksheetApplier

//    @Binds
//    @P6Singleton
//    fun WorkbookEventApplier(i: WorkbookEventApplierImp): WorkbookEventApplier
    @Binds
    @P6Singleton
    fun DeleteWorksheetInternalApplier(i: DeleteWorksheetInternalApplierImp): DeleteWorksheetInternalApplier

    @Binds
    @P6Singleton
    fun DeleteWorksheetApplier(i: DeleteWorksheetApplierImp): DeleteWorksheetApplier

    @Binds
    @P6Singleton
    fun NewWorksheetInternalApplier(i: NewWorksheetInternalApplierImp): NewWorksheetInternalApplier

    @Binds
    @P6Singleton
    fun NewWorksheetApplier(i: NewWorksheetApplierImp): NewWorksheetApplier
}
