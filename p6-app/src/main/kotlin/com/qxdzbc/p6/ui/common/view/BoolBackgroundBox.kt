package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * A box that has background color changes base on a boolean
 * @param [colorIfTrue] the background color when the boolean is 1
 * @param [colorIfFalse] the background color when the boolean is 0
 * TODO this can be turned into a Modifier
 */
@Composable
fun BoolBackgroundBox(
    boolValue: Boolean,
    colorIfTrue:Color,
    modifier: Modifier = Modifier,
    colorIfFalse:Color = Color.Transparent,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val color = if (boolValue) colorIfTrue else colorIfFalse
    MBox(modifier = modifier
        .background(color)
    ) {
        content()
    }
}
