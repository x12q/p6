package com.emeraldblast.p6.app.action.applier

import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.common.utils.RseNav
import com.emeraldblast.p6.app.common.utils.ErrorUtils.noNav
import com.emeraldblast.p6.app.communication.res_req_template.response.Response
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.emeraldblast.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.emeraldblast.p6.app.communication.res_req_template.response.ScriptResponse
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import javax.inject.Inject

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
