package com.qxdzbc.p6.rpc.worksheet

import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.ui.document.cell.action.UpdateCellAction

interface WorksheetRpcAction:
    PasteRangeAction,UpdateCellAction,
    DeleteMultiCellAction, LoadDataAction,
    RemoveAllCellAction,MultiCellUpdateAction
