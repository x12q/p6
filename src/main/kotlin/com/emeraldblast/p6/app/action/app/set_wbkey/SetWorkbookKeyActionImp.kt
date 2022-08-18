package com.emeraldblast.p6.app.action.app.set_wbkey

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.action.app.set_wbkey.applier.SetWbKeyApplier
import com.emeraldblast.p6.app.action.app.set_wbkey.rm.SetWbKeyRM
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyRequest
import com.emeraldblast.p6.rpc.document.workbook.msg.SetWbKeyResponse
import javax.inject.Inject

class SetWorkbookKeyActionImp @Inject constructor(
    val rm: SetWbKeyRM,
    val applier: SetWbKeyApplier,
) : SetWorkbookKeyAction {
    override fun setWbKeyRs(req: SetWbKeyRequest) : Rse<SetWbKeyResponse> {
        val rmRs = rm.setWbKeyRequest(req)
        val applyRs=applier.applySetWbKey(rmRs)
        return applyRs
    }
}
