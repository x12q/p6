package com.qxdzbc.p6.app.action.applier

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWorkbookKeyTemplate
import com.qxdzbc.p6.app.communication.res_req_template.response.ScriptResponse
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event
import javax.inject.Inject

/**
 * For testing only. This applier ignores errors and simply run the callback
 */
class FakeJustRunErrorApplier @Inject constructor() : ErrorApplier {
    override fun <R : ScriptResponse> applyScriptRes(res: R, onOk: (R) -> Unit) {
        onOk(res)
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3(res: R, event: P6Event, onOk: (R) -> Unit) {
        onOk(res)
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3(res: R, onOk: (R) -> Unit) {
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2(res: R, event: P6Event, onOk: (R) -> Unit) {
        onOk(res)
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2(res: R, onOk: (R) -> Unit) {
    }

    override fun <R : ScriptResponse> applyScriptResRs(res: R): Rse<R> {
        TODO("Not yet implemented")
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3Rs(res: R, event: P6Event): Rse<R> {
        TODO("Not yet implemented")
    }

    override fun <R : ResponseWithWindowIdAndWorkbookKey> apply3Rs(res: R): Rse<R> {
        TODO("Not yet implemented")
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2Rs(res: R, event: P6Event): Rse<R> {
        TODO("Not yet implemented")
    }

    override fun <R : ResponseWithWorkbookKeyTemplate> apply2Rs(res: R): Rse<R> {
        TODO("Not yet implemented")
    }

}
