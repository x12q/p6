package com.qxdzbc.p6.rpc.worksheet

import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.composite_actions.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.composite_actions.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.composite_actions.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.composite_actions.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.composite_actions.cell.cell_update.UpdateCellAction

interface WorksheetRpcAction:
    PasteRangeAction, UpdateCellAction,
    DeleteMultiCellAction, LoadDataAction,
    RemoveAllCellAction,UpdateMultiCellAction
