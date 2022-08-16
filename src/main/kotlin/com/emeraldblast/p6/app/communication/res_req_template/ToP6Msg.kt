package com.emeraldblast.p6.app.communication.res_req_template

import com.emeraldblast.p6.app.communication.event.P6EventTableImp
import com.emeraldblast.p6.app.communication.res_req_template.request.remote.ToProtoBytes
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Messages

/**
 * Something that can be converted into a [P6Message]
 */
interface ToP6Msg : ToProtoBytes {
    fun toP6Msg(): P6Message {
        val tb = P6EventTableImp
        return P6Messages.p6Message(
            event = tb.findEventFor(this),
            data = this.toProtoBytes()
        )
    }
}

