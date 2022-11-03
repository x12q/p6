package com.qxdzbc.p6.app.action.applier

import com.qxdzbc.common.Rs
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.app.communication.res_req_template.response.Response
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.communication.res_req_template.response.ScriptResponse
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class BaseApplierImp @Inject constructor(
    private val errorApplier: ErrorApplier,
    private val errorRouter: ErrorRouter,
) : BaseApplier {
    override fun <T : Response> applyRes(res: T?, onResOk: (res: T) -> Unit) {
        when (res) {
            is ResponseWithWindowIdAndWorkbookKey -> errorApplier.apply3(res, onResOk)
            is ResponseWithWorkbookKeyTemplate -> errorApplier.apply2(res, onResOk)
            is ScriptResponse -> errorApplier.applyScriptRes(res, onResOk)
        }
    }

    override fun <T : Response> applyResRs(res: T?): Rs<T, ErrorReport> {
        return when (res) {
            is ResponseWithWindowIdAndWorkbookKey -> errorApplier.apply3Rs(res)
            is ResponseWithWorkbookKeyTemplate -> errorApplier.apply2Rs(res)
            is ScriptResponse -> errorApplier.applyScriptResRs(res)
            else -> throw IllegalArgumentException("unsupported response")
        }
    }

    override fun <T> applyResRsNoNav(res: RseNav<T>): Rs<T, ErrorReport> {
        return applyResRsKeepNav(res).noNav()
    }

    override fun <T> applyResRsKeepNav(res: RseNav<T>): RseNav<T> {
        when (res) {
            is Ok -> return res
            is Err -> {
                errorRouter.publish(res.error)
                return res
            }
        }
    }
}
