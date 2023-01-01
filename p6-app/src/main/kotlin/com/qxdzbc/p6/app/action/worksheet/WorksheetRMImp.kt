package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.worksheet.delete_multi.rm.DeleteMultiRM
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm.UpdateMultiCellRM
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding

import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorksheetRM::class)
class WorksheetRMImp @Inject constructor(
    private val dm: DeleteMultiRM,
    private val umc: UpdateMultiCellRM,
) : WorksheetRM,
    DeleteMultiRM by dm, UpdateMultiCellRM by umc
