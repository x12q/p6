package com.qxdzbc.p6.ui.worksheet.state

/**
 * This bridge interface is to prevent circular reference when a child object of [WorksheetState] attempt to access [WorksheetState] itself.
 * To access their parent [WorksheetState], child objects within [WorksheetState] must use this instead
 */
fun interface WorksheetStateGetter {
    fun get():WorksheetState?
}