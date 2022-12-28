package com.qxdzbc.common.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

object ColorUtils {
    fun Color.getContrastColor(): Color {
        return this.copy(
            red = 1 - this.red,
            green = 1 - this.green,
            blue = 1 - this.blue,
        )
    }

    /**
     * @return either color black or white based on the luminance of a color (threshold is 0.45).
     */
    fun Color.getBlackOrWhiteOnLuminance(): Color {
        val luminace = try {
            this.luminance()
        } catch (e: Exception) {
            Color(this.toArgb()).luminance()
        }
        if (luminace >= 0.45) {
            return Color.Black
        } else {
            return Color.White
        }
    }
}
