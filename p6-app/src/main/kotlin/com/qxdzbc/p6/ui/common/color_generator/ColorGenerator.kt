package com.qxdzbc.p6.ui.common.color_generator

import androidx.compose.ui.graphics.Color

/**
 * Generate colors
 */
interface ColorGenerator {
    /**
     * return a color on each call
     */
    fun nextColor():Color
}
