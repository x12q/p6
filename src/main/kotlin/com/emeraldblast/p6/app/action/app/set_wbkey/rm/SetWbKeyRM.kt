package com.emeraldblast.p6.app.action.app.set_wbkey.rm

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyResponse

interface SetWbKeyRM {
    fun setWbKeyRequest(req:SetWbKeyRequest):RseNav<SetWbKeyResponse>
}
