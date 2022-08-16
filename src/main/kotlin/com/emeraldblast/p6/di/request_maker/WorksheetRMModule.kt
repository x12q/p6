package com.emeraldblast.p6.di.request_maker

import com.emeraldblast.p6.app.action.worksheet.WorksheetRM
import com.emeraldblast.p6.app.action.worksheet.WorksheetRMImp
import com.emeraldblast.p6.app.action.worksheet.delete_cell.rm.DeleteCellRM
import com.emeraldblast.p6.app.action.worksheet.delete_cell.rm.DeleteCellRMImp
import com.emeraldblast.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.emeraldblast.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRMImp
import com.emeraldblast.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.emeraldblast.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRMImp
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRMImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM as UpdateMultiCellRM1

@dagger.Module
interface WorksheetRMModule {

    @Binds
    @P6Singleton
    
    fun UpdateMultiCellRMLocal(i: UpdateMultiCellRMImp): UpdateMultiCellRM1

    @Binds
    @P6Singleton
    
    fun RenameWorksheetRMLocal(i: RenameWorksheetRMImp): RenameWorksheetRM

    @Binds
    @P6Singleton
    
    fun DeleteMultiRMLocal(i: DeleteMultiRMImp): DeleteMultiRM

    @Binds
    @P6Singleton
    
    fun DeleteCellRMLocal(i: DeleteCellRMImp): DeleteCellRM

    @Binds
    @P6Singleton
    fun bindWorksheetRequestMaker(mk: WorksheetRMImp): WorksheetRM

}
