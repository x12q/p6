package com.emeraldblast.p6.di.applier

import com.emeraldblast.p6.app.action.worksheet.WorkbookUpdateApplier
import com.emeraldblast.p6.app.action.worksheet.WorkbookUpdateApplierImp
import com.emeraldblast.p6.app.action.worksheet.delete_cell.applier.DeleteCellResponseApplier
import com.emeraldblast.p6.app.action.worksheet.delete_cell.applier.DeleteCellResponseApplierImp
import com.emeraldblast.p6.app.action.worksheet.delete_cell.applier.DeleteCellResponseInternalApplier
import com.emeraldblast.p6.app.action.worksheet.delete_cell.applier.DeleteCellResponseInternalApplierImp
import com.emeraldblast.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplier
import com.emeraldblast.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplierImp
import com.emeraldblast.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetInternalApplier
import com.emeraldblast.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetInternalApplierImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds
import dagger.Module

@Module
interface WorksheetApplierModule {
    @Binds
    @P6Singleton
    fun WorkbookUpdateApplier(i: WorkbookUpdateApplierImp): WorkbookUpdateApplier

    @Binds
    @P6Singleton
    fun DeleteCellResponseInternalApplier(i: DeleteCellResponseInternalApplierImp): DeleteCellResponseInternalApplier

    @Binds
    @P6Singleton
    fun DeleteCellResponseApplier(i: DeleteCellResponseApplierImp): DeleteCellResponseApplier

    @Binds
    @P6Singleton
    fun RenameWorksheetInternalApplier(i: RenameWorksheetInternalApplierImp): RenameWorksheetInternalApplier

    @Binds
    @P6Singleton
    fun RenameWorksheetApplier(i: RenameWorksheetApplierImp): RenameWorksheetApplier
}
