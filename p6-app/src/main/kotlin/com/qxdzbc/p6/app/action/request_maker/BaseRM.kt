package com.qxdzbc.p6.app.action.request_maker

import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response


/**
 * Provide functions to send [P6Message] with built-in error handling at [P6Response] level.
 * Error of the content is not handled.
 * The reason this maker does not handle error, but allow error report handling injection is that: each request can be handled on difference places (window or app) that are unknown at P6Message level
 */
interface BaseRM {
    fun send(p6Message: P6Message): P6Response
    fun <T> sendThenCheck(
        p6Msg: P6Message,
        onOk: (p6Res: P6Response) -> T?,
        onErr: (p6Res: P6Response) -> Unit,
    ): T?
}


