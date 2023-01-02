package com.qxdzbc.p6.ui.window.tool_bar.color_selector.state

interface ColorSelectorInternalState {
    val expanded: Boolean
    fun setExpanded(i:Boolean): ColorSelectorInternalState

    val openColorDialog: Boolean
    fun setOpenColorDialog(i:Boolean): ColorSelectorInternalState
}
