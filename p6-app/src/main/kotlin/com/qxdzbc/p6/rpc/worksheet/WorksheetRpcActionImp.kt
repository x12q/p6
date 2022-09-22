package com.qxdzbc.p6.rpc.worksheet

import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import javax.inject.Inject

class WorksheetRpcActionImp @Inject constructor(
    private val pasteAction: PasteRangeAction,
    private val updateCell: UpdateCellAction,
    private val deleteMultiCell: DeleteMultiCellAction,
    val ldt: LoadDataAction,
    val rmAllCell: RemoveAllCellAction,
    val mcuAct:MultiCellUpdateAction,
) : WorksheetRpcAction,
    MultiCellUpdateAction by mcuAct,
    PasteRangeAction by pasteAction,
    UpdateCellAction by updateCell,
    DeleteMultiCellAction by deleteMultiCell,
    LoadDataAction by ldt,
    RemoveAllCellAction by rmAllCell
