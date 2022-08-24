package com.qxdzbc.p6.app.action.app.set_wbkey

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.rpc.document.workbook.msg.SetWbKeyRequest
import com.qxdzbc.p6.rpc.document.workbook.msg.SetWbKeyResponse

interface SetWorkbookKeyAction {
    fun setWbKeyRs(req:SetWbKeyRequest): Rs<SetWbKeyResponse, ErrorReport>
}
