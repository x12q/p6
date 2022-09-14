package com.qxdzbc.p6.ui.document.workbook.state.cont

import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.github.michaelbull.result.map

abstract class AbsWorkbookStateContainer : WorkbookStateContainer {
    @kotlin.jvm.Throws(Exception::class)
    override fun createNewWbState(wb: Workbook): WorkbookStateContainer {
        return this.createNewWbStateRs(wb).getOrThrow()
    }
    override fun containWbKey(wbKey: WorkbookKey): Boolean {
        return this.getWbStateMs(wbKey)!=null
    }
    override fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState> {
        return this.getWbStateMsRs(wbKeySt).map { it.value }
    }
    override fun getWbStateMs(wbKeySt: St<WorkbookKey>): Ms<WorkbookState>? {
        return this.getWbStateMsRs(wbKeySt).component1()
    }
    override fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        return getWbStateMsRs(wbKey).map { it.value }
    }

    override fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return getWbStateRs(wbKey).component1()
    }

    override fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState? {
        return this.getWbStateRs(wbKeySt).component1()
    }
}
