package com.qxdzbc.p6.rpc

import com.qxdzbc.common.Rse
import io.grpc.Server

interface P6RpcServer {
    val port:Int
    val server:Server?
    fun start(): Rse<Unit>
    fun stop(): Rse<Unit>
}
