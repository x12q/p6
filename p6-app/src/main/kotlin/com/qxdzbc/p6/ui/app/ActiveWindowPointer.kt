package com.qxdzbc.p6.ui.app

/**
 * Point to the window that is currently on top.
 */
interface ActiveWindowPointer {
    val windowId:String?
    fun pointTo(windowId:String?):ActiveWindowPointer
    fun nullify():ActiveWindowPointer
}


