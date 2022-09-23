package com.qxdzbc.p6.app.action.remote_request_maker

import com.qxdzbc.p6.app.communication.res_req_template.ToP6Msg
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.*
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import javax.inject.Inject

/**
 * Provide sending method for a number of template request
 */
class TemplateRMImp @Inject constructor(
    private val base: BaseRemoteRM,
    private val p6ErrHandler: P6ResponseErrorHandler,
) : TemplateRM {

    override fun <I : ScriptRequest, O> makeScriptRequest(req: I, parse: (P6Response) -> O?): O? {
        return this.makeReq(
            req = req,
            parse = parse,
            onError = {
                p6ErrHandler.publishErrResponseOnScriptWindow(it)
            }
        )
    }

    override fun <I : RemoteRequest, O> makeRequest0(req: I, parse: (P6Response) -> O?): O? {
        return this.makeReq(req = req, parse = parse, onError = {
            p6ErrHandler.publishErrResponseOnApp(it)
        })
    }

    /**
     * make request call for request obj that can:
     * - produces a p6Msg
     * - contains a windowId
     */
    override fun <I : RemoteRequestToP6WithWindowId, O> makeRequest1(req: I, parse: (P6Response) -> O?): O? {
        return this.makeReq(req = req, parse = parse, onError = {
            p6ErrHandler.publishErrResponseOnWindow(it, req.windowId)
        })
    }

    /**
     * make request call for request obj that can:
     * - produces a p6Msg
     * - contains a workbookKey
     */
    override fun <I : RemoteRequestToP6WithWorkbookKey, O> makeRequest2(req: I, parse: (P6Response) -> O?): O? {
        return this.makeReq(req = req, parse = parse, onError = {
            p6ErrHandler.publishErrResponseOnWindow(it, req.wbKey)
        })
    }

    /**
     * make request call for request obj that can:
     * - produces a p6Msg
     * - contains a workbookKey and a window id
     */
    override fun <I: RemoteRequestToP6WithWorkbookKeyAndWindowId, O> makeRequest3(req: I, parse: (P6Response) -> O?): O? {
        return this.makeReq(req = req, parse = parse, onError = {
            p6ErrHandler.publishErrResponseOnWindow(it, req.windowId, req.wbKey)
        })
    }

    private fun <I : ToP6Msg, O> makeReq(req: I, parse: (P6Response) -> O?, onError: (P6Response) -> Unit): O? {
        return base.sendThenCheck(
            p6Msg = req.toP6Msg(),
            onOk = {
                parse(it)
            },
            onErr = {
                onError(it)
            }
        )
    }
}
