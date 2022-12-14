package com.qxdzbc.p6.ui.common.view.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.qxdzbc.common.compose.view.MBox

@Composable
fun IconBox(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    onClick: () -> Unit,
) {
    MBox(modifier = modifier
        .clickable {
            if(enable){
                onClick()
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = modifier
                .align(Alignment.Center),
        )
    }
}
