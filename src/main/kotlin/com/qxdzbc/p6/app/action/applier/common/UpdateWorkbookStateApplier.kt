package com.qxdzbc.p6.app.action.applier.common

import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState

interface UpdateWorkbookStateApplier {
    fun updateWbState(newWbState:WorkbookState?)
}
