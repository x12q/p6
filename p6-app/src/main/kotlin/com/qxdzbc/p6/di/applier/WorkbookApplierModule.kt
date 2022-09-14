package com.qxdzbc.p6.di.applier


import com.qxdzbc.p6.app.action.workbook.add_ws.applier.CreateNewWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.add_ws.applier.CreateNewWorksheetApplierImp
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplierImp
import com.qxdzbc.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplierImp
import com.qxdzbc.p6.app.action.workbook.new_worksheet.applier.NewWorksheetInternalApplier
import com.qxdzbc.p6.app.action.workbook.new_worksheet.applier.NewWorksheetInternalApplierImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WorkbookApplierModule {
    @Binds
    @P6Singleton
    fun AddWsApplier(i: CreateNewWorksheetApplierImp): CreateNewWorksheetApplier

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
