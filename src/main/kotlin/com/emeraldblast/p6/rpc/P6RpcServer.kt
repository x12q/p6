package com.emeraldblast.p6.rpc

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.common.exception.error.ErrorReport
import io.grpc.Server

interface P6RpcServer {
    val port:Int
    val server:Server?
    fun start():Rs<Unit,ErrorReport>
    fun stop():Rs<Unit,ErrorReport>
}
