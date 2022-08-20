package com.emeraldblast.p6.ui.document.workbook.state.cont

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState

interface WorkbookStateContainer : Map<St<WorkbookKey>,Ms<WorkbookState>>{

    val allStatesMs:List<Ms<WorkbookState>>
    val allStates:List<WorkbookState>

    fun getWbStateMsRs(wbKey:WorkbookKey): Rse<Ms<WorkbookState>>
    fun getWbStateMs(wbKey:WorkbookKey):Ms<WorkbookState>?
    fun getWbStateRs(wbKey:WorkbookKey): Rse<WorkbookState>
    fun getWbState(wbKey:WorkbookKey):WorkbookState?

    fun getWbStateMsRs(wbKeySt:St<WorkbookKey>): Rse<Ms<WorkbookState>>
    fun getWbStateMs(wbKeySt:St<WorkbookKey>):Ms<WorkbookState>?
    fun getWbStateRs(wbKeySt:St<WorkbookKey>): Rse<WorkbookState>
    fun getWbState(wbKeySt:St<WorkbookKey>):WorkbookState?

    fun addWbState(wbStateMs:Ms<WorkbookState>): WorkbookStateContainer
    fun removeWbState(wbKey: WorkbookKey): WorkbookStateContainer
    fun updateWbState(newWbState:WorkbookState):WorkbookStateContainer
    fun removeAll():WorkbookStateContainer
    fun replaceKey(oldWbKey:WorkbookKey, newWbKey:WorkbookKey):WorkbookStateContainer

    fun containWbKey(wbKey: WorkbookKey):Boolean

    fun createNewWbStateRs(wb:Workbook): Rse<WorkbookStateContainer>
    fun createNewWbState(wb:Workbook):WorkbookStateContainer
}
