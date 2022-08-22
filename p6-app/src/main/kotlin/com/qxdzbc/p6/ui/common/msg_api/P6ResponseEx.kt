package com.qxdzbc.p6.ui.common.msg_api

import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.Status

fun P6Response.isError():Boolean{
    return status == Status.ERROR
}

fun P6Response.isOk():Boolean{
    return status == Status.OK
}
