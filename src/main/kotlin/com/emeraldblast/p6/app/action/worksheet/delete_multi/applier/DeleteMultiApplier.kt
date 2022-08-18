package com.emeraldblast.p6.app.action.worksheet.delete_multi.applier

import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiResponse2
import com.emeraldblast.p6.app.common.utils.RseNav

interface DeleteMultiApplier {
    fun apply(res: RseNav<DeleteMultiResponse2>): RseNav<DeleteMultiResponse2>
}
