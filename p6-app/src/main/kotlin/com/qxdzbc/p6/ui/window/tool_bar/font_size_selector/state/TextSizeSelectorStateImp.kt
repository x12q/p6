package com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.state

data class TextSizeSelectorStateImp(
    override val headerText: String,
) : TextSizeSelectorState {

    override fun setHeaderText(i: String): TextSizeSelectorState {
        return this.copy(headerText = i)
    }
}
