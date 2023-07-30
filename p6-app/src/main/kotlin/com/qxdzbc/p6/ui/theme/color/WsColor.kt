package com.qxdzbc.p6.ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Color of UI element inside worksheet
 */
@Immutable
data class WsColor(
    val cellDivider: Color = Color.LightGray,
    val cellBackGround:Color = Color.White,
    val cursorColor:Color = Color.Blue,
){
    companion object{
        fun light():WsColor{
            return WsColor()
        }
        fun dark():WsColor{
            return WsColor(
                // TODO add dark color
            )
        }
        val local = staticCompositionLocalOf(
            defaultFactory = {
                light()
            }
        )
    }
}