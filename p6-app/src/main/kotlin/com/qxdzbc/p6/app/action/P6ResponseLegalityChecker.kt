package com.qxdzbc.p6.app.action

import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response

/**
 * check legality of a P6Response
 */
interface P6ResponseLegalityChecker {
    /**
     * @return true if everything is ok, false if there are legality violations
     */
    fun check(p6Res: P6Response, expect: P6Event):Boolean
}
