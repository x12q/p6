package com.qxdzbc.p6.ui.workbook.state.cont

import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.workbook.state.WorkbookState

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
