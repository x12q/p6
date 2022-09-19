package com.qxdzbc.p6.rpc.worksheet

import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.ui.document.cell.action.UpdateCellAction
import javax.inject.Inject

class WorksheetRpcActionImp @Inject constructor(
    private val pasteAction: PasteRangeAction,
    private val updateCell: UpdateCellAction,
    private val deleteMultiCell: DeleteMultiCellAction,
    val ldt:LoadDataAction,
) : WorksheetRpcAction,
        PasteRangeAction by pasteAction,
    UpdateCellAction by updateCell,
    DeleteMultiCellAction by deleteMultiCell,
        LoadDataAction by ldt
