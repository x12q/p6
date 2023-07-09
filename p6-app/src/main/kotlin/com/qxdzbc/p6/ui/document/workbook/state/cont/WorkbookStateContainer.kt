package com.qxdzbc.p6.ui.document.workbook.state.cont

import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState

interface WorkbookStateContainer : Map<St<WorkbookKey>,WorkbookState>, WorkbookStateGetter{

    val allWbStates:List<WorkbookState>

    fun addOrOverwriteWbState(wbStateMs:WorkbookState): WorkbookStateContainer
    fun removeWbState(wbKey: WorkbookKey): WorkbookStateContainer
    fun updateWbState(newWbState:WorkbookState):WorkbookStateContainer
    fun removeAll():WorkbookStateContainer

    fun replaceKeyRs(oldWbKey:WorkbookKey, newWbKey:WorkbookKey):Rse<WorkbookStateContainer>
    fun replaceKey(oldWbKey:WorkbookKey, newWbKey:WorkbookKey):WorkbookStateContainer

    fun containWbKey(wbKey: WorkbookKey):Boolean

    fun createNewWbStateRs(wb:Workbook): Rse<WorkbookStateContainer>
    fun createNewWbState(wb:Workbook):WorkbookStateContainer
}
