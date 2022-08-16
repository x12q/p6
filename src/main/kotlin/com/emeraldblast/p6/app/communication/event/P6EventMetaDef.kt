package com.emeraldblast.p6.app.communication.event

import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Event

/**
 * Contain anything that can be used to look up a [P6Event] from [P6EventTable]
 */
interface P6EventMetaDef {
    val event: P6Event
    val Request:Any?
    val Response:Any
    val others:List<Any> get()= emptyList()
}

