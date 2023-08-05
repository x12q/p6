package com.qxdzbc.p6.ui.document.workbook.state.factory

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState


/**
 * Can create new [WorkbookState]. A singleton instance is available globally in the DI graph.
 * See [WorkbookStateFactoryImp] for implementation.
 */
interface WorkbookStateFactory {

    /**
     * Create a new [WorkbookState]
     */
    fun create(
        wbMs: Ms<Workbook>,
        windowId: String?,
    ): WorkbookState
}
