package com.qxdzbc.p6.app.action.remote_request_maker

import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response

/**
 * Send a [P6Message] by pushing the request into a queue
 */
interface QueueRequestMaker {
    suspend fun send(p6Message: P6Message): P6Response
    suspend fun <T> sendThenCheck(
        p6Msg: P6Message,
        onOk: (p6Res: P6Response) -> T?,
        onErr: (p6Res: P6Response) -> Unit,
    ): T?
}
