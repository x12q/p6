package com.qxdzbc.p6.ui.window.tool_bar.font_size_selector.action

interface TextSizeSelectorAction{
    fun submitManualEdit(windowId:String,value:String)
    fun pickItemFromList(windowId:String, item:Int)
    fun setHeaderTextOfTextSizeSelector(windowId:String, newText:String)
}
