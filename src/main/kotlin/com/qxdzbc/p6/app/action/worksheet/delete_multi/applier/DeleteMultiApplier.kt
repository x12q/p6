package com.qxdzbc.p6.app.action.worksheet.delete_multi.applier

import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiResponse2
import com.qxdzbc.p6.app.common.utils.RseNav

interface DeleteMultiApplier {
    fun apply(res: RseNav<DeleteMultiResponse2>): RseNav<DeleteMultiResponse2>
}
