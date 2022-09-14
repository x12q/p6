package com.qxdzbc.p6.app.action.applier

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.communication.event.P6EventErrors
import com.qxdzbc.p6.app.communication.event.P6EventTable
import com.qxdzbc.p6.app.communication.event.P6EventTableImp
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.communication.res_req_template.response.ScriptResponse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject

class ErrorApplierImp @Inject constructor(
    val errorRouter: ErrorRouter,
    val p6EventTable: P6EventTable = P6EventTableImp,
) : ErrorApplier {

    override fun <R : ScriptResponse> applyScriptRes(res: R, onOk: (R) -> Unit) {
        this.applyScriptResRs(res).onSuccess(onOk)
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3(
        res: R,
        event: P6Event,
        onOk: (R) -> Unit
    ) {
        this.apply3Rs(res,event).onSuccess(onOk)
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3(res: R, onOk: (R) -> Unit) {
        this.apply3Rs(res).onSuccess(onOk)
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2(
        res: R,
        event: P6Event,
        onOk: (R) -> Unit
    ) {
        this.apply2Rs(res,event).onSuccess(onOk)
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2(res: R, onOk: (R) -> Unit) {
        this.apply2Rs(res).onSuccess {
            onOk(it)
        }
    }

    override fun <R : ScriptResponse> applyScriptResRs(res: R): Rse<R> {
        if (res.isError) {
            errorRouter.publishToScriptWindow(res.errorReport)
            val z= res.errorReport ?: CommonErrors.Unknown.report("")
            return z.toErr()
        } else {
            return Ok(res)
        }
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3Rs(res: R, event: P6Event): Rse<R> {
        return this.publishErr3Rs(res, event)
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3Rs(res: R): Rse<R> {
        val rt = p6EventTable.findEventForRs(res).andThen { e ->
            this.apply3Rs(res,e)
        }
        return rt
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2Rs(res: R, event: P6Event): Rse<R> {
        return this.publishErr2Rs(res,event)
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2Rs(res: R): Rse<R> {
        val rt = p6EventTable.findEventForRs(res).andThen { e ->
            this.apply2Rs(res,e)
        }
        return rt
    }


    fun publishErr3(res: ResponseWithWindowIdAndWorkbookKey, event: P6Event): Boolean {
        return this.publishErr3Rs(res,event) is Ok
    }

    fun <R : ResponseWithWindowIdAndWorkbookKey> publishErr3Rs(res: R, event: P6Event): Rse<R> {
        if (res.isLegal().not()) {
            val err = P6EventErrors.IllegalStateError(event)
            errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            return err.toErr()
        }

        val r = res.errorReport
        if (r!=null) {
            errorRouter.publishToWindow(r, res.windowId, res.wbKey)
            return r.toErr()
        }
        return Ok(res)
    }

    fun publishErr2(res: ResponseWithWorkbookKeyTemplate, event: P6Event): Boolean {
        return this.publishErr2Rs(res,event) is Ok
    }

    fun <R:ResponseWithWorkbookKeyTemplate>publishErr2Rs(res: R, event: P6Event): Rse<R> {
        if (res.isLegal().not()) {
            val err = P6EventErrors.IllegalStateError(event)
            errorRouter.publishToWindow(err, res.wbKey)
            return err.toErr()
        }

        if (res.isError) {
            errorRouter.publishToWindow(res.errorReport, res.wbKey)
            return res.errorReport!!.toErr()
        }
        return Ok(res)
    }
}
