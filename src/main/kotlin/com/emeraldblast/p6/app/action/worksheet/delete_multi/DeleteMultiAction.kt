package com.emeraldblast.p6.app.action.worksheet.delete_multi

import com.emeraldblast.p6.app.common.utils.RseNav

interface DeleteMultiAction {
    fun deleteMulti2(request:DeleteMultiRequest2): RseNav<DeleteMultiResponse2>
}
