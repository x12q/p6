package com.qxdzbc.p6.rpc

import com.qxdzbc.common.Rs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.status_bar.RPCStatusItemStateQualifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.rpc.MsRpcServerQualifier
import com.qxdzbc.p6.proto.rpc.AppServiceGrpc
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * A rpc server that is tied to a Ms state
 */
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
@MsRpcServerQualifier
class MsP6RpcServer @Inject constructor(
    private val rpcServer: P6RpcServer,
    @RPCStatusItemStateQualifier
    private val viewStateMs: Ms<RPCStatusViewState>
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
