package com.qxdzbc.p6.app.action.worksheet.delete_multi.applier

import com.qxdzbc.p6.app.action.worksheet.delete_multi.RemoveMultiCellResponse
import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiApplier {
    fun apply(res: RseNav<RemoveMultiCellResponse>): RseNav<RemoveMultiCellResponse>
}
