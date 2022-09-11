package com.qxdzbc.p6.app.action.app.set_wbkey.applier

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.app.set_wbkey.SetWbKeyResponse

interface SetWbKeyApplier {
    fun applySetWbKey(res: RseNav<SetWbKeyResponse>): Rse<SetWbKeyResponse>
}
