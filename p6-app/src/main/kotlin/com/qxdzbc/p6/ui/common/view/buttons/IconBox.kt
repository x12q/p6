package com.qxdzbc.p6.ui.common.view.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox

@Composable
fun IconBox(
    icon: ImageVector,
    iconSize: Int = 20,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    modifier: Modifier = Modifier,
    ) {
    MBox(
        modifier = modifier.size(iconSize.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = "",
            modifier = modifier.align(Alignment.Center)
        )
    }
}
