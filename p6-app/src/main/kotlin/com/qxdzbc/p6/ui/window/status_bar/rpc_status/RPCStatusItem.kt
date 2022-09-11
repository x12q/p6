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
import com.qxdzbc.common.compose.view.MBox


@Composable
fun RPCStatusView(
    state:RPCStatusViewState,
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
