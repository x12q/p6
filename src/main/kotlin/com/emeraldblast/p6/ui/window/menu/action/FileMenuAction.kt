package com.emeraldblast.p6.ui.window.menu.action

interface FileMenuAction{
    fun newWorkbook(windowId: String)
    fun save(windowId: String)
    fun saveAs(windowId: String)
    fun open(windowId: String)
}
