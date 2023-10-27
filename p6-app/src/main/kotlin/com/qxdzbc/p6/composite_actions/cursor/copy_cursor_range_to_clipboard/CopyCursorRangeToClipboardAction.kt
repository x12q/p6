package com.qxdzbc.p6.composite_actions.cursor.copy_cursor_range_to_clipboard

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt

interface CopyCursorRangeToClipboardAction{
    /**
     * Compute a range (often the main range) from a cursor's state at [wbws], and copy it to clipboard
     */
    fun copyCursorRangeToClipboard(wbws: WbWs)
    /**
     * Compute a range (often the main range) from a cursor's state at [wbwsSt], and copy it to clipboard
     */
    fun copyCursorRangeToClipboard(wbwsSt: WbWsSt)
}
