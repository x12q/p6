package com.qxdzbc.p6.ui.common.view.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MIconButton(
    icon: ImageVector,
    enabled:Boolean = true,
    modifier: Modifier = Modifier,
    onClick:()->Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ){
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = modifier
        )
    }
}
