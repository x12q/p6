package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.action.worksheet.rename_ws.rm.RenameWorksheetRM
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding

import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorksheetRM::class)
class WorksheetRMImp @Inject constructor(
    private val dm: DeleteMultiRM,
    private val rws: RenameWorksheetRM,
    private val umc: UpdateMultiCellRM,
) : WorksheetRM,
    DeleteMultiRM by dm, RenameWorksheetRM by rws, UpdateMultiCellRM by umc
