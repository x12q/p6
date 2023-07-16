package com.qxdzbc.p6.ui.document.workbook.state.cont

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState

interface WorkbookStateContainer : WorkbookStateGetter{

    val allWbStates:List<WorkbookState>

    fun addOrOverwriteWbState(wbStateMs:WorkbookState)

    fun removeWbState(wbKey: WorkbookKey)

    fun updateWbState(newWbState:WorkbookState)

    fun removeAll()

    fun replaceKeyRs(oldWbKey:WorkbookKey, newWbKey:WorkbookKey): Rse<Unit>

    fun replaceKey(oldWbKey:WorkbookKey, newWbKey:WorkbookKey)

    fun containWbKey(wbKey: WorkbookKey):Boolean

    fun createNewWbStateRs(wb:Workbook): Rse<Unit>

    fun createNewWbState(wb:Workbook)
}
