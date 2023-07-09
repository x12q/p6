package com.qxdzbc.p6.ui.document.workbook.state.cont

import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState

abstract class AbsWorkbookStateContainer : WorkbookStateContainer {

    @Throws(Exception::class)
    override fun createNewWbState(wb: Workbook) {
        return this.createNewWbStateRs(wb).getOrThrow()
    }

    override fun containWbKey(wbKey: WorkbookKey): Boolean {
        return this.getWbState(wbKey)!=null
    }

    override fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState? {
        return this.getWbStateRs(wbKeySt).component1()
    }

    override fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        return getWbStateRs(wbKey)
    }

    override fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return getWbStateRs(wbKey).component1()
    }

}
