package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.worksheet.delete_cell.rm.DeleteCellRM
import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM

interface WorksheetRM : DeleteCellRM, DeleteMultiRM, RenameWorksheetRM, UpdateMultiCellRM {
}
