package com.qxdzbc.p6.rpc.worksheet

import com.qxdzbc.p6.composite_actions.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.composite_actions.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.composite_actions.worksheet.load_data.LoadDataAction
import com.qxdzbc.p6.composite_actions.worksheet.paste_range.PasteRangeAction
import com.qxdzbc.p6.composite_actions.worksheet.remove_all_cell.RemoveAllCellAction
import com.qxdzbc.p6.composite_actions.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.di.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
