package com.emeraldblast.p6.ui.document.workbook.state.cont

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map

data class WorkbookStateContainerImp constructor(
    private val wbStateFactory: WorkbookStateFactory,
    val m: Map<Ms<WorkbookKey>, Ms<WorkbookState>> = emptyMap(),
    private val pseudoVar:Boolean = false,
) : WorkbookStateContainer, Map<Ms<WorkbookKey>, Ms<WorkbookState>> by m {


    override fun getWbStateMs(wbKey: WorkbookKey): Ms<WorkbookState>? {
        return this.m.values.firstOrNull{it.value.wbKey == wbKey}
    }

    override val allStatesMs: List<Ms<WorkbookState>>
        get() = this.m.values.toList()

    override val allStates: List<WorkbookState>
        get() = allStatesMs.map{it.value}


    override fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return getWbStateRs(wbKey).component1()
    }

    override fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        return getWbStateMsRs(wbKey).map { it.value }
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        val w = this.m.values.firstOrNull{it.value.wbKey==wbKey}
        return w?.let { Ok(it) } ?: Err(WorkbookStateContainerErrors.WorkbookStateNotExist.report(
            "workbook state for key ${wbKey} does not exist"
        ))
    }

    override fun addWbState(wbStateMs: Ms<WorkbookState>): WorkbookStateContainer {
        val newMap = this.m + (wbStateMs.value.wb.keyMs to wbStateMs)
        return this.copy(m = newMap)
    }

    override fun removeWbState(wbKey: WorkbookKey): WorkbookStateContainer {
        return this.copy(m = m.filter { it.key.value!=wbKey })
    }

    /**
     * Create a pseudo copy
     */
    private fun forceRefresh():WorkbookStateContainer{
        return this
    }

    override fun updateWbState(newWbState: WorkbookState): WorkbookStateContainer {
        val sms=this.getWbStateMs(newWbState.wbKey)
        if(sms!=null){
            sms.value = newWbState
            return forceRefresh()
        }else{
            return this.addWbState(ms(newWbState))
        }
    }

    override fun removeAll(): WorkbookStateContainer {
        return this.copy(m = emptyMap())
    }

    override fun replaceKey(oldWbKey: WorkbookKey, newWbKey: WorkbookKey): WorkbookStateContainer {
        val wbStateMs = this.getWbStateMs(oldWbKey)
        if (wbStateMs != null) {
            wbStateMs.value = wbStateMs.value.setWorkbookKey(newWbKey)
            return this.removeWbState(oldWbKey).addWbState(wbStateMs)
        }
        return this
    }

    override fun containWbKey(wbKey: WorkbookKey): Boolean {
        return this.getWbStateMs(wbKey)!=null
    }

    override fun createNewWbStateRs(wb: Workbook): Rse<WorkbookStateContainer> {
        if(this.containWbKey(wb.key)){
            return WorkbookStateContainerErrors.WorkbookStateAlreadyExist.report("Can't create new workbook state for ${wb.key} because a state for such key already exist").toErr()
        }else{
            val newWbState = wbStateFactory.create(ms(wb))
            return Ok(this.addWbState(ms(newWbState)))
        }
    }

    override fun createNewWbState(wb: Workbook): WorkbookStateContainer {
        return this.createNewWbStateRs(wb).getOrThrow()
    }
}
