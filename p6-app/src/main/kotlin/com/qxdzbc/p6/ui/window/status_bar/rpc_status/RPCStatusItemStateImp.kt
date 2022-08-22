package com.qxdzbc.p6.ui.window.status_bar.rpc_status

data class RPCStatusItemStateImp(
    override val isRunning: Boolean = false,
    override val port: Int?=null,
    override val isShowingDetail: Boolean = false,
) : RPCStatusItemState {

    override fun setRunning(v: Boolean): RPCStatusItemState {
        return this.copy(isRunning = v)
    }

    override fun setPort(p: Int): RPCStatusItemState {
        return this.copy(port = p)
    }

    override fun showDetail(): RPCStatusItemState {
        return this.copy(isShowingDetail=true)
    }

    override fun hideDetail(): RPCStatusItemState {
        return this.copy(isShowingDetail=false)
    }
}
