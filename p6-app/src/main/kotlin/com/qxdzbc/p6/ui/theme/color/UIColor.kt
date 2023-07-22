package com.qxdzbc.p6.ui.theme.color

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Color of UI elements in most part of the app other than worksheet. Such as:
 * - button colors
 * - ui text colors
 */
@Immutable
data class UIColor(
    val uiBackground: Color = Color.White,
    val uiText: Color = Color.Black,
    val uiDisabledText: Color = Color.LightGray,
    val highlightedButton: Color = Color(0xFF1b47b5),
    val disabledButton: Color = Color.LightGray,
    val enabledButton:Color = Color.Gray,
    val borderColor:Color = Color.Black,
    val iconColor: Color = Color.Black,
    val uiBaseSurface:Color = Color.LightGray,
    val selectedTabBackground: Color = Color.Cyan,
    val rulerBackground: Color= Color.LightGray,
    val buttonBorder: Color = Color.Black
){

    companion object {
        val textSelectionColors:TextSelectionColors = TextSelectionColors(
            handleColor = Color(0xFF4286F4),
            backgroundColor = Color(0xFF4286F4).copy(alpha = 0.4f)
        )
        fun light():UIColor{
            return UIColor()
        }
        fun dark():UIColor{
            return UIColor(
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