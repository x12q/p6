package com.qxdzbc.p6.app.communication.event

import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event

object P6EventErrors {
    val P6EErr ="UI_P6EventErrors_"
    fun wrongEventError(expect: P6Event, actual:P6Event):ErrorReport{
        return ErrorReport(
            header = ErrorHeader(
                errorCode = "${P6EErr}0",
                errorDescription = "Expect ${expect.toString()}, but get ${actual.toString()}"
            )
        )
    }
    fun IllegalStateError(event:P6Event):ErrorReport{
        return ErrorReport(
            header = ErrorHeader(
                errorCode = "${P6EErr}1",
                errorDescription = "data of event ${event.code} is not in a legal state"
            )
        )
    }
    fun UnknownError():ErrorReport{
        return ErrorReport(
            header = ErrorHeader(
                errorCode = "${P6EErr}2",
                errorDescription = "Unknown error"
            )
        )
    }
    fun NoEventForObj(obj:Any):ErrorReport{
        return ErrorReport(
            header = ErrorHeader(
                errorCode = "${P6EErr}3",
                errorDescription = "Can't find event for ${obj} "
            )
        )
    }
}
