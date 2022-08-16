package com.emeraldblast.p6.ui.app

/**
 * Point to the currently on top window
 */
interface ActiveWindowPointer {
    val windowId:String?
    fun pointTo(windowId:String?):ActiveWindowPointer
    fun nullify():ActiveWindowPointer
}


