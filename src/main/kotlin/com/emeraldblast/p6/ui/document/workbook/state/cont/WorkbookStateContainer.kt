package com.emeraldblast.p6.ui.document.workbook.state.cont

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookId
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState

interface WorkbookStateContainer : Map<Ms<WorkbookKey>,Ms<WorkbookState>>{

    val allStatesMs:List<Ms<WorkbookState>>
    val allStates:List<WorkbookState>

    fun getWbStateMsRs(wbKey:WorkbookKey):Rse<Ms<WorkbookState>>
    fun getWbStateMsRs(wbId:WorkbookId):Rse<Ms<WorkbookState>>
    fun getWbStateMs(wbKey:WorkbookKey):Ms<WorkbookState>?
    fun getWbStateMs(wbId:WorkbookId):Ms<WorkbookState>?

    fun getWbState(wbKey:WorkbookKey):WorkbookState?
    fun getWbState(wbId:WorkbookId):WorkbookState?
    fun getWbStateRs(wbKey:WorkbookKey):Rse<WorkbookState>
    fun getWbStateRs(wbId:WorkbookId):Rse<WorkbookState>

    fun addWbState(wbStateMs:Ms<WorkbookState>): WorkbookStateContainer
    fun removeWbState(wbKey: WorkbookKey): WorkbookStateContainer
    fun updateWbState(newWbState:WorkbookState):WorkbookStateContainer
    fun removeAll():WorkbookStateContainer
    fun replaceKey(oldWbKey:WorkbookKey, newWbKey:WorkbookKey):WorkbookStateContainer

    fun containWbKey(wbKey: WorkbookKey):Boolean

    fun createNewWbStateRs(wb:Workbook):Rse<WorkbookStateContainer>
    fun createNewWbState(wb:Workbook):WorkbookStateContainer
}
