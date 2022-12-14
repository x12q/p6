package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.qxdzbc.common.compose.view.MBox

@Composable
fun ToggleIconBox(
    checked:Boolean,
    checkedIcon:ImageVector,
    unCheckedIcon:ImageVector,
    modifier: Modifier = Modifier,
    onCheckedChange:(Boolean)->Unit = {},
) {
    MBox(modifier = modifier.clickable {
        onCheckedChange(!checked)
    }) {
        Icon(
            imageVector = if (checked) checkedIcon else unCheckedIcon,
            contentDescription = "toggle icon button",
            modifier = Modifier
                .align(Alignment.Center),
            )
    }
}
