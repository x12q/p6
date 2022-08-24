package com.qxdzbc.p6.rpc

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import io.grpc.Server

interface P6RpcServer {
    val port:Int
    val server:Server?
    fun start(): Rs<Unit, ErrorReport>
    fun stop(): Rs<Unit, ErrorReport>
}
