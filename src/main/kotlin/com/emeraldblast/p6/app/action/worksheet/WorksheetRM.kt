package com.emeraldblast.p6.app.action.worksheet

import com.emeraldblast.p6.app.action.worksheet.delete_cell.rm.DeleteCellRM
import com.emeraldblast.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.emeraldblast.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM

interface WorksheetRM : DeleteCellRM, DeleteMultiRM, RenameWorksheetRM, UpdateMultiCellRM {
}
