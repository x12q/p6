package com.qxdzbc.p6.app.action.request_maker

import com.qxdzbc.p6.app.communication.res_req_template.request.remote.*
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import javax.inject.Inject

/**
 * Provide sending method for a number of template request
 */
class TemplateRMSuspendImp @Inject constructor(
    private val base: QueueRequestMaker,
    private val p6ErrHandler: P6ResponseErrorHandler,
) : TemplateRMSuspend {
    override suspend fun <I : RemoteRequest, O> makeRequest0(req: I, parse: (P6Response) -> O?): O? {
        return base.sendThenCheck(
            p6Msg = req.toP6Msg(),
            onOk = {
                parse(it)
            },
            onErr = {
                p6ErrHandler.publishErrResponseOnApp(it)
            }
        )
    }

    /**
     * make request call for request obj that can:
     * - produces a p6Msg
     * - contains a windowId
     */
    override suspend fun <I, O> makeRequest1(req: I, parse: (P6Response) -> O?): O?
            where I : RemoteRequestToP6WithWindowId {
        return base.sendThenCheck(
            p6Msg = req.toP6Msg(),
            onOk = {
                parse(it)
            },
            onErr = {
                p6ErrHandler.publishErrResponseOnWindow(it, req.windowId)
            }
        )
    }

    /**
     * make request call for request obj that can:
     * - produces a p6Msg
     * - contains a workbookKey
     */
    override suspend fun <I, O> makeRequest2(req: I, parse: (P6Response) -> O?): O?
            where I : RemoteRequestToP6WithWorkbookKey {
        return base.sendThenCheck(
            p6Msg = req.toP6Msg(),
            onOk = {
                parse(it)
            },
            onErr = {
                p6ErrHandler.publishErrResponseOnWindow(it, req.wbKey)
            }
        )
    }

    /**
     * make request call for request obj that can:
     * - produces a p6Msg
     * - contains a workbookKey and a window id
     */
    override suspend fun <I, O> makeRequest3(req: I, parse: (P6Response) -> O?): O?
            where I : RemoteRequestToP6WithWorkbookKeyAndWindowId {
        return base.sendThenCheck(
            p6Msg = req.toP6Msg(),
            onOk = {
                parse(it)
            },
            onErr = {
                p6ErrHandler.publishErrResponseOnWindow(it, req.windowId, req.wbKey)
            }
        )
    }
}
