package com.qxdzbc.p6.app.action.window.tool_bar

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

interface UpdateFormatIndicator {
    fun updateFormatIndicator(wbWsSt: WbWsSt)
}
