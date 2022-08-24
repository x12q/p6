package com.qxdzbc.p6.rpc

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.status_bar.RPCStatusItemStateQualifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusItemState
import com.github.michaelbull.result.Ok
import javax.inject.Inject

/**
 * A rpc server that is tied to a Ms state
 */
class MsP6RpcServer @Inject constructor(
    private val rpcServer: P6RpcServer,
    @RPCStatusItemStateQualifier
    private val viewStateMs: Ms<RPCStatusItemState>
) : P6RpcServer by rpcServer{

    override fun start(): Rs<Unit, ErrorReport> {
        val rt=this.rpcServer.start()
        viewStateMs.value = viewStateMs.value.setPort(this.port).setRunning(rt is Ok)
        return rt
    }

    override fun stop(): Rs<Unit, ErrorReport> {
        val rt = this.rpcServer.stop()
        viewStateMs.value = viewStateMs.value.setPort(this.port).setRunning(rt !is Ok)
        return rt
    }
}
