package com.qxdzbc.p6.ui.window.status_bar.rpc_status

interface RPCStatusViewState {
    val isRunning: Boolean
    fun setRunning(v: Boolean): RPCStatusViewState
    val port: Int?
    fun setPort(p: Int): RPCStatusViewState
    val isShowingDetail:Boolean
    fun showDetail(): RPCStatusViewState
    fun hideDetail(): RPCStatusViewState
}
