package com.qxdzbc.p6.ui.window.status_bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.status_bar.KernelStatusItemStateQualifier
import com.qxdzbc.p6.di.status_bar.RPCStatusItemStateQualifier
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusViewState
import javax.inject.Inject

class StatusBarStateImp @Inject constructor(
    @RPCStatusItemStateQualifier
    override val rpcServerStatusStateMs: Ms<RPCStatusViewState>
) : StatusBarState {
    override var rpcServerStatusState: RPCStatusViewState by rpcServerStatusStateMs

}
