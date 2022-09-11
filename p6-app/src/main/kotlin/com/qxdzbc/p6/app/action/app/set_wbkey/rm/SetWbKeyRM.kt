package com.qxdzbc.p6.app.action.app.set_wbkey.rm

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyRequest
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyResponse

interface SetWbKeyRM {
    fun setWbKeyRequest(req: SetWbKeyRequest): RseNav<SetWbKeyResponse>
}
