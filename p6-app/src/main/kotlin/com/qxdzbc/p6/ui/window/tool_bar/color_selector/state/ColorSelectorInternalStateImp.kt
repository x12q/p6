package com.qxdzbc.p6.ui.window.tool_bar.color_selector.state

data class ColorSelectorInternalStateImp(
    override val expanded: Boolean = false,
    override val openColorDialog: Boolean = false
) : ColorSelectorInternalState {
    override fun setExpanded(i: Boolean): ColorSelectorInternalStateImp {
        return this.copy(expanded=i)
    }

    override fun setOpenColorDialog(i: Boolean): ColorSelectorInternalStateImp {
        return this.copy(openColorDialog=i)
    }
}
