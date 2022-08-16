package com.emeraldblast.p6.ui.window.status_bar

import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.window.status_bar.kernel_status.KernelStatusItemState
import com.emeraldblast.p6.ui.window.status_bar.rpc_status.RPCStatusItemState

interface StatusBarState {
    val kernelStatusItemStateMs:Ms<KernelStatusItemState>
    var kernelStatusItemState:KernelStatusItemState

    val rpcServerStatusStateMs:Ms<RPCStatusItemState>
    var rpcServerStatusState:RPCStatusItemState
}
