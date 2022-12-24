package com.qxdzbc.p6.ui.window.tool_bar.color_selector.state

import androidx.compose.ui.graphics.Color

data class ColorSelectorStateImp(override val currentColor: Color) : ColorSelectorState {
    override fun setCurrentColor(i: Color): ColorSelectorState {
        return this.copy(currentColor = i)
    }

}
