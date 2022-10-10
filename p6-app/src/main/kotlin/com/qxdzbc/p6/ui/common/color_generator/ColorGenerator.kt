package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

/**
 * Generate a single color
 */
interface ColorGenerator {
    fun nextColor():Color
}
