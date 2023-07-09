package com.qxdzbc.p6.ui.document.workbook.state.cont

import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState

interface WorkbookStateGetter{
    fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState>
    fun getWbState(wbKey: WorkbookKey): WorkbookState?

    fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState>
    fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState?
}