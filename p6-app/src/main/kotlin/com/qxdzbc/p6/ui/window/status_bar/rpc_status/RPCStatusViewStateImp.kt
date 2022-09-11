package com.qxdzbc.p6.ui.window.status_bar.rpc_status

data class RPCStatusViewStateImp(
    override val isRunning: Boolean = false,
    override val port: Int?=null,
    override val isShowingDetail: Boolean = false,
) : RPCStatusViewState {

    override fun setRunning(v: Boolean): RPCStatusViewState {
        return this.copy(isRunning = v)
    }

    override fun setPort(p: Int): RPCStatusViewState {
        return this.copy(port = p)
    }

    override fun showDetail(): RPCStatusViewState {
        return this.copy(isShowingDetail=true)
    }

    override fun hideDetail(): RPCStatusViewState {
        return this.copy(isShowingDetail=false)
    }
}
