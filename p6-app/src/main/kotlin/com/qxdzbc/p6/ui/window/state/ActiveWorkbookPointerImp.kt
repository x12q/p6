package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class ActiveWorkbookPointerImp(override val wbKeyMs: Ms<WorkbookKey>?) : ActiveWorkbookPointer {

    override val wbKey: WorkbookKey? get()=wbKeyMs?.value

    override fun isPointingTo(workbookKey: WorkbookKey): Boolean {
        return this.wbKey == workbookKey
    }

    override fun isPointingTo(wbKeyMs: St<WorkbookKey>): Boolean {
        return this.wbKeyMs == wbKeyMs
    }

    override fun nullify(): ActiveWorkbookPointer {
        return this.copy(wbKeyMs=null)
    }

    override fun pointTo(wbKeyMs: Ms<WorkbookKey>?): ActiveWorkbookPointer {
        return this.copy(wbKeyMs=wbKeyMs)
    }
}
