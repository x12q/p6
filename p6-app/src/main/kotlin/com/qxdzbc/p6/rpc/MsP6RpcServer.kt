package com.qxdzbc.p6.rpc

import com.qxdzbc.p6.di.status_bar.RPCStatusItemStateQualifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.rpc.MsRpcServerQualifier
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A rpc server that is tied to a Ms state
 */
@Singleton
@ContributesBinding(P6AnvilScope::class)
@MsRpcServerQualifier
class MsP6RpcServer @Inject constructor(
    private val rpcServer: P6RpcServer,
    @RPCStatusItemStateQualifier
    private val viewStateMs: Ms<RPCStatusViewState>
) : P6RpcServer by rpcServer{

    override fun start(): Rse<Unit> {
        val rt=this.rpcServer.start()
        viewStateMs.value = viewStateMs.value.setPort(this.port).setRunning(rt is Ok)
        return rt
    }

    override fun stop(): Rse<Unit> {
        val rt = this.rpcServer.stop()
        viewStateMs.value = viewStateMs.value.setPort(this.port).setRunning(rt !is Ok)
        return rt
    }
}
