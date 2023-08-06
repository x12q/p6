package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.worksheet.Worksheet

interface WorksheetStateFactory {
    fun create(wsMs: Ms<Worksheet>): WorksheetState
}