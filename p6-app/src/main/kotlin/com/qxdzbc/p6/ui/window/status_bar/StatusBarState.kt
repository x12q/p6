package com.qxdzbc.p6.ui.window.status_bar

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemState
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState

interface StatusBarState {
    val kernelStatusItemStateMs:Ms<KernelStatusItemState>
    var kernelStatusItemState:KernelStatusItemState

    val rpcServerStatusStateMs:Ms<RPCStatusViewState>
    var rpcServerStatusState:RPCStatusViewState
}
