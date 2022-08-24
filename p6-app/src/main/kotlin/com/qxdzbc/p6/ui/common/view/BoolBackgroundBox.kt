package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.view.MBox

/**
 * A box that has background color changes base on a boolean
 * @param [color1] the background color when the boolean is 1
 * @param [color0] the background color when the boolean is 0
 */
@Composable
fun BoolBackgroundBox(
    boolValue: Boolean,
    color0:Color = Color.Transparent,
    color1:Color = MaterialTheme.colors.secondaryVariant,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val color = if (boolValue) color1 else color0
    MBox(modifier = modifier
        .background(color)
    ) {
        content()
    }
}
