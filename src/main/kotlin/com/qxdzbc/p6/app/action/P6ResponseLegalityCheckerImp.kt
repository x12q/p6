package com.qxdzbc.p6.app.action

import com.qxdzbc.p6.app.communication.event.P6Events
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import com.qxdzbc.p6.ui.app.ErrorRouter
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.unwrapError
import javax.inject.Inject

class P6ResponseLegalityCheckerImp @Inject constructor(
    private val errorRouter: ErrorRouter,
) : P6ResponseLegalityChecker {

    fun checkEvent(p6Res: P6Response,expect: P6Event): Boolean {
        val rs = P6Events.checkEventRs(p6Res.header.eventType, expect)
        if (rs is Ok){
            return true
        }else{
            errorRouter.publishToApp(rs.unwrapError())
            return false
        }
    }

    /**
     * Check for legality of a P6Response, publish error if detect in-consistency
     */
    override fun check(p6Res: P6Response, expect: P6Event): Boolean {
        val correctEvent = this.checkEvent(p6Res,expect)
        return correctEvent
    }
}
