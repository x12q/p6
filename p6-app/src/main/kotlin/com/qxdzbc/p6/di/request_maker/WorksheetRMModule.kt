package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.worksheet.WorksheetRM
import com.qxdzbc.p6.app.action.worksheet.WorksheetRMImp
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRMImp
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRMImp
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRMImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM as UpdateMultiCellRM1

@dagger.Module
interface WorksheetRMModule {

//    @Binds
//    @P6Singleton
//    fun UpdateMultiCellRM1(i: UpdateMultiCellRMImp): UpdateMultiCellRM1
//
//    @Binds
//    @P6Singleton
//    fun RenameWorksheetRM(i: RenameWorksheetRMImp): RenameWorksheetRM
//
//    @Binds
//    @P6Singleton
//    fun DeleteMultiRM(i: DeleteMultiRMImp): DeleteMultiRM
//
//
//    @Binds
//    @P6Singleton
//    fun WorksheetRM(mk: WorksheetRMImp): WorksheetRM

}
