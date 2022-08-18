package com.emeraldblast.p6.app.action.worksheet.delete_multi.rm

import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiRequest2
import com.emeraldblast.p6.app.action.worksheet.delete_multi.DeleteMultiResponse2
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.DeleteMultiRequest
import com.emeraldblast.p6.app.action.worksheet.update_multi_cell.DeleteMultiResponse

interface DeleteMultiRM {
    fun deleteMulti(request: DeleteMultiRequest): DeleteMultiResponse?
    fun deleteMulti2(request: DeleteMultiRequest2): RseNav<DeleteMultiResponse2>
}
