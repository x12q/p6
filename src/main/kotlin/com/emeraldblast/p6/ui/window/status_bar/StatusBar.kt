package com.emeraldblast.p6.ui.window.status_bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.window.status_bar.rpc_status.RPCStatusItem

@Composable
fun StatusBar(
    state:StatusBarState
){
    Row{
        KernelStatusItem(state.kernelStatusItemState, onClick={
            state.kernelStatusItemState = state.kernelStatusItemState.showDetail()
        })
        Spacer(modifier = Modifier.width(10.dp))
        RPCStatusItem(
            state = state.rpcServerStatusState,
            onClick = {
                state.rpcServerStatusState = state.rpcServerStatusState.showDetail()
            }
        )
    }
}
