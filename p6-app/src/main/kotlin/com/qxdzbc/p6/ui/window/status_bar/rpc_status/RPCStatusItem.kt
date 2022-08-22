package com.qxdzbc.p6.ui.window.status_bar.rpc_status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.view.MBox


interface RPCStatusItemState {
    val isRunning: Boolean
    fun setRunning(v: Boolean): RPCStatusItemState
    val port: Int?
    fun setPort(p: Int): RPCStatusItemState
    val isShowingDetail:Boolean
    fun showDetail():RPCStatusItemState
    fun hideDetail():RPCStatusItemState
}

@Composable
fun RPCStatusItem(
    state:RPCStatusItemState,
    onClick:()->Unit,
) {
    MBox(modifier = Modifier.fillMaxHeight().padding(start = 5.dp).clickable {
        onClick()
    }) {
        val text = if (state.isRunning) {
            "RPC server is running"
        } else {
            "RPC server is not available"
        }
        BasicText(text, modifier = Modifier.align(Alignment.CenterStart), overflow = TextOverflow.Ellipsis)
    }
}