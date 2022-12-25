package com.qxdzbc.p6.ui.window.tool_bar.color_selector.state

import androidx.compose.ui.graphics.Color

interface ColorSelectorState{
    val currentColor: Color?
    fun setCurrentColor(i:Color?): ColorSelectorState
}
