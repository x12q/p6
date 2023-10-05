package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.theme.P6Theme

@Composable
fun BrowseButton(
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val c =  MaterialTheme.colors.onPrimary
    MBox {
        MBox(modifier = Modifier.align(Alignment.CenterEnd).clickable(enabled=enabled) {
            if (enabled) {
                onClick()
            }
        }) {
            Box(modifier = Modifier.border(width = 1.dp, color = c, shape = P6Theme.shape.textFieldShape)) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "Browse",
                    modifier = Modifier.align(Alignment.Center).padding(1.dp),
                    tint = c
                )
            }
        }
    }
}

fun main() {
    testApp {
        BrowseButton { }
    }
}
