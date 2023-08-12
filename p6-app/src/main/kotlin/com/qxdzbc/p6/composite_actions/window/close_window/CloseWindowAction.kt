package com.qxdzbc.p6.composite_actions.window.close_window

import com.qxdzbc.common.Rse

interface WithWindowId{
    val windowId:String
}
interface CloseWindowAction {
    fun closeWindow(windowId: String):Rse<Unit>
    fun closeWindow(withWindowId: WithWindowId):Rse<Unit>
}
