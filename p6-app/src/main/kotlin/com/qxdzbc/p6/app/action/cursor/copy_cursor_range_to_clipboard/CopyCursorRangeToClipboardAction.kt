package com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

interface CopyCursorRangeToClipboardAction{
    /**
     * Compute a range from a cursor's state (often the main range), and copy it to clipboard
     */
    fun copyCursorRangeToClipboard(wbws: WbWs)
    fun copyCursorRangeToClipboard(wbwsSt: WbWsSt)
}
