package com.qxdzbc.p6.ui.worksheet.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet

interface WorksheetStateFactory {
    fun create(wsMs: Ms<Worksheet>): WorksheetState
}