package com.qxdzbc.p6.app.action.worksheet.check_range_selector_state


@Deprecated("to be deleted, don't use, kept just in case")
interface CheckRangeSelectorStateAction {
    /**
     * Check if range selector should be enabled/allowed for a certain text and a cursor at a certain position.
     * @return true if range selector should be enabled, false otherwise
     */
    fun check(
        selectorText:String,
        selectorCursorPosition:Int
    ):Boolean
}
