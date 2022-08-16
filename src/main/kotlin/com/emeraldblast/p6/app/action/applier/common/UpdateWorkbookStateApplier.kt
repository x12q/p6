package com.emeraldblast.p6.app.action.applier.common

import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState

interface UpdateWorkbookStateApplier {
    fun updateWbState(newWbState:WorkbookState?)
}
