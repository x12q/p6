package com.emeraldblast.p6.ui.document.workbook.state.cont

import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.github.michaelbull.result.map

abstract class AbsWorkbookStateContainer : WorkbookStateContainer {
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
