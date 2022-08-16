package com.emeraldblast.p6.app.action.app.set_wbkey.applier

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyResponse

interface SetWbKeyApplier {
    fun applySetWbKey(res: RseNav<SetWbKeyResponse>): Rse<SetWbKeyResponse>
}
