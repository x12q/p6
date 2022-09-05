package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM

import javax.inject.Inject

class WorksheetRMImp @Inject constructor(
    private val dm: DeleteMultiRM,
    private val rws: RenameWorksheetRM,
    private val umc: UpdateMultiCellRM,
) : WorksheetRM,
    DeleteMultiRM by dm, RenameWorksheetRM by rws, UpdateMultiCellRM by umc
