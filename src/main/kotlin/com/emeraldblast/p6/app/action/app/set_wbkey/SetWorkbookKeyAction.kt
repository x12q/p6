package com.emeraldblast.p6.app.action.app.set_wbkey

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyResponse

interface SetWorkbookKeyAction {
    fun setWbKeyRs(req:SetWbKeyRequest): Rs<SetWbKeyResponse, ErrorReport>
}
