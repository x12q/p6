package com.qxdzbc.p6.app.action.request_maker

import com.qxdzbc.p6.app.communication.res_req_template.request.Request
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RemoteRequest
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWindowId
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.qxdzbc.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKeyAndWindowId
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response


/**
 * Provide functions to perform requests (to backend) on certain request template.
 * If there are errors during making request or in the received [P6Response], the functions will publish the errors to the appropriate channel, and return null. Otherwise, return a parsed object
 */
interface TemplateRMSuspend {
    suspend fun <I : RemoteRequest, O> makeRequest0(req: I, parse: (P6Response) -> O?): O?
    suspend fun <I : RequestToP6WithWindowId, O> makeRequest1(req: I, parse: (P6Response) -> O?): O?
    suspend fun <I : RequestToP6WithWorkbookKey, O> makeRequest2(req: I, parse: (P6Response) -> O?): O?
    suspend fun <I : RequestToP6WithWorkbookKeyAndWindowId, O> makeRequest3(req: I, parse: (P6Response) -> O?): O?
}
