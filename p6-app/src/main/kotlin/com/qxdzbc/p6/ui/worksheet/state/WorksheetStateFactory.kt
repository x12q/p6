package com.qxdzbc.p6.ui.worksheet.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet

/**
 * Can create [WorksheetState].
 * [WorksheetStateFactory] is available as a singleton in the global DI container.
 */
interface WorksheetStateFactory {
    /**
     * Create a [WorksheetState] for [wsMs]
     */
    fun create(wsMs: Ms<Worksheet>): WorksheetState

}