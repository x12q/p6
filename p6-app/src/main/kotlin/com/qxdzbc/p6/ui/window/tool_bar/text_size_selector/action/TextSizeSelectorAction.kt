package com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action

interface TextSizeSelectorAction{
    fun submitManualEdit(windowId:String,value:String)
    fun pickTextSize(windowId:String, textSize:Int)
    fun setHeaderTextOfTextSizeSelector(windowId:String, newText:String)
}
