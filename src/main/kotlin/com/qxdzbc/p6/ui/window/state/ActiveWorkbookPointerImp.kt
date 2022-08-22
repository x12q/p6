package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class ActiveWorkbookPointerImp(override val wbKey: WorkbookKey?) : ActiveWorkbookPointer {
    override fun isPointingTo(workbookKey: WorkbookKey): Boolean {
        return this.wbKey == workbookKey
    }

    override fun nullify(): ActiveWorkbookPointer {
        return this.copy(wbKey=null)
    }

    override fun pointTo(workbookKey: WorkbookKey?): ActiveWorkbookPointer {
        return this.copy(wbKey=workbookKey)
    }

}
