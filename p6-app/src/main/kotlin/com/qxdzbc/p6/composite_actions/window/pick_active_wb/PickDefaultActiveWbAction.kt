package com.qxdzbc.p6.composite_actions.window.pick_active_wb

import com.qxdzbc.p6.ui.window.state.WindowState

/**
 * Attempt to pick a new active wb pointer for a window state if the current pointer is pointing to no workbook. Do nothing otherwise.
 */
interface PickDefaultActiveWbAction {
    fun pickAndUpdateActiveWbPointer(windowState:WindowState)
    fun pickAndUpdateActiveWbPointer(windowId:String?)
}
