package com.qxdzbc.p6.ui.common.modifier

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color

/**
 * A background that changes color base on a boolean
 * @param [colorIfTrue] the background color when the boolean is 1
 * @param [colorIfFalse] the background color when the boolean is 0
 */
@Composable
fun Modifier.boolBackground(
    boolValue: Boolean,
    colorIfTrue: Color,
    colorIfFalse: Color = Color.Transparent,
): Modifier {
    val mod = composed{
        val color = if (boolValue) colorIfTrue else colorIfFalse
        background(color)
    }
    return mod
}