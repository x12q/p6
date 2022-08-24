package com.qxdzbc.p6.ui.window.status_bar

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
import com.qxdzbc.p6.ui.window.status_bar.kernel_status.KernelStatusItemState

@Composable
fun KernelStatusItem(
    state: KernelStatusItemState,
    onClick: () -> Unit = {},
) {
    MBox(modifier = Modifier.fillMaxHeight().padding(start = 5.dp).clickable {
        onClick()
    }) {
        val text = if (state.kernelIsRunning) {
            "Kernel is running"
        } else {
            "Kernel is not available"
        }
        BasicText(text, modifier = Modifier.align(Alignment.CenterStart), overflow = TextOverflow.Ellipsis)
    }
}
