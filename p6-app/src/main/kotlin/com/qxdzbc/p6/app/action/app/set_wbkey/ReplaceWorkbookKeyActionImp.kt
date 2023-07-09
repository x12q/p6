package com.qxdzbc.p6.app.action.app.set_wbkey

import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouters.publishErrIfNeedSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@ContributesBinding(P6AnvilScope::class)
class ReplaceWorkbookKeyActionImp @Inject constructor(
    val stateCont:StateContainer,
    val errorRouter: ErrorRouter,
) : ReplaceWorkbookKeyAction {

   

    override fun replaceWbKey(req: SetWbKeyRequest) : Rse<Unit> {
        val oldKey = req.wbKey
        val newKey = req.newWbKey
        val rs = stateCont.getWbRs(oldKey).flatMap { oldWb->
            val qRs = stateCont.wbStateCont.replaceKeyRs(oldKey,newKey)
            qRs
        }
        rs.publishErrIfNeedSt(errorRouter,)
        return rs.map { Unit }

    }
}
