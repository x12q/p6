package com.qxdzbc.p6.rpc

import com.qxdzbc.common.Rse
import io.grpc.Server

/**
 * A simple interface providing information about the current RPC server in the app. Also allow control that server.
 */
interface P6RpcServer {
    val port:Int
    val server:Server?
    fun start(): Rse<Unit>
    fun stop(): Rse<Unit>
}
