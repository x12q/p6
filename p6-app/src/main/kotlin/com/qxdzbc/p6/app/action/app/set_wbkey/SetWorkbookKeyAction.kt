package com.qxdzbc.p6.app.action.app.set_wbkey

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport

interface SetWorkbookKeyAction {
    fun setWbKeyRs(req: SetWbKeyRequest): Rs<SetWbKeyResponse, ErrorReport>
}
