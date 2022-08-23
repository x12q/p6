package com.qxdzbc.p6.ui.window.status_bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.rpc.ReactiveRpcServerQualifier
import com.qxdzbc.p6.di.status_bar.KernelStatusItemStateQualifier
import com.qxdzbc.p6.di.status_bar.RPCStatusItemStateQualifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemState
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusItemState
import javax.inject.Inject

class StatusBarStateImp @Inject constructor(
    @KernelStatusItemStateQualifier
    override val kernelStatusItemStateMs: Ms<KernelStatusItemState>,
    @RPCStatusItemStateQualifier
    override val rpcServerStatusStateMs: Ms<RPCStatusItemState>
) : StatusBarState {
    override var kernelStatusItemState: KernelStatusItemState by kernelStatusItemStateMs
    override var rpcServerStatusState: RPCStatusItemState by rpcServerStatusStateMs

}
