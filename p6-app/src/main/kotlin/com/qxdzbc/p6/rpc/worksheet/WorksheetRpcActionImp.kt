package com.qxdzbc.p6.rpc.worksheet

import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.app.action.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType =WorksheetRpcAction::class)
class WorksheetRpcActionImp @Inject constructor(
    private val pasteAction: PasteRangeAction,
    private val updateCell: UpdateCellAction,
    private val deleteMultiCell: DeleteMultiCellAction,
    val ldt: LoadDataAction,
    val rmAllCell: RemoveAllCellAction,
    val mcuAct:UpdateMultiCellAction,
) : WorksheetRpcAction,
    UpdateMultiCellAction by mcuAct,
    PasteRangeAction by pasteAction,
    UpdateCellAction by updateCell,
    DeleteMultiCellAction by deleteMultiCell,
    LoadDataAction by ldt,
    RemoveAllCellAction by rmAllCell
