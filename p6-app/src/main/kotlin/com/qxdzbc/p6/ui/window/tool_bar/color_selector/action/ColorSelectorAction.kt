package com.qxdzbc.p6.ui.window.tool_bar.color_selector.action

import androidx.compose.ui.graphics.Color

interface ColorSelectorAction{
    fun clearColor(windowId:String)
    fun pickColor(windowId:String,color: Color)
}
