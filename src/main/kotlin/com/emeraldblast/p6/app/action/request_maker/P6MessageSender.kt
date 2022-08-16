package com.emeraldblast.p6.app.action.request_maker

import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.emeraldblast.p6.message.api.connection.service.zmq_services.msg.P6Response
import org.zeromq.ZMQ


interface P6MessageSender {
    /**
     * Send a [P6Message] using a socket, and return a [P6Response]
     */
    fun send(p6Mesg: P6Message, socket: ZMQ.Socket): P6Response
}

