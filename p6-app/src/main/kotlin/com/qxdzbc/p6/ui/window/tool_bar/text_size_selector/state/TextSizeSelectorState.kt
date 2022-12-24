package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state

interface TextSizeSelectorState {
    val headerText:String
    fun setHeaderText(i:String): TextSizeSelectorState
    companion object{
        const val defaultHeader = "13"
    }
}
