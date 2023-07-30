package com.qxdzbc.p6.ui.document.workbook.state.cont

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType = WorkbookStateContainer::class)
data class WorkbookStateContainerImp(
    private val wbStateFactory: WorkbookStateFactory,
    val mapMs: Ms<Map<St<WorkbookKey>, WorkbookState>>,
    private val pseudoVar: Boolean,
) : AbsWorkbookStateContainer() {

    @Inject
    constructor(
        wbStateFactory: WorkbookStateFactory
    ) : this(wbStateFactory, ms(emptyMap()), false)

    val map: Map<St<WorkbookKey>, WorkbookState> by mapMs

    override fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return this.map.values.firstOrNull { it.wbKey == wbKey }
    }

    override val allWbStates: List<WorkbookState>
        get() = this.map.values.toList()


    override fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        val w = this.map.values.firstOrNull { it.wbKey == wbKey }
        return w?.let { Ok(it) } ?: Err(
            WorkbookStateContainerErrors.WorkbookStateNotExist.report(
                "workbook state for key ${wbKey} does not exist"
            )
        )
    }

    override fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState> {
        val w = this.map[wbKeySt]
        return w?.let { Ok(it) } ?: Err(
            WorkbookStateContainerErrors.WorkbookStateNotExist.report(
                "workbook state for key ${wbKeySt} does not exist"
            )
        )
    }

    override fun addOrOverwriteWbState(wbStateMs: WorkbookState) {
        val newMap = this.map + (wbStateMs.wb.keyMs to wbStateMs)
        mapMs.value = newMap
    }

    override fun removeWbState(wbKey: WorkbookKey) {
        mapMs.value = map.filter { it.key.value != wbKey }
    }

    override fun updateWbState(newWbState: WorkbookState) {
        val sms = this.getWbState(newWbState.wbKey)
        if (sms == null) {
            this.addOrOverwriteWbState(newWbState)
        }
    }

    override fun removeAll() {
        mapMs.value = emptyMap()
    }

    override fun replaceKeyRs(oldWbKey: WorkbookKey, newWbKey: WorkbookKey): Rse<Unit> {
        val rt = this.getWbStateRs(oldWbKey).map { wbStateMs ->
            wbStateMs.setWbKey(newWbKey)
            this.removeWbState(oldWbKey)
            this.addOrOverwriteWbState(wbStateMs)
        }
        return rt
    }

    override fun replaceKey(oldWbKey: WorkbookKey, newWbKey: WorkbookKey) {
        replaceKeyRs(oldWbKey, newWbKey)
    }

    override fun createNewWbStateRs(wb: Workbook): Rse<Unit> {
        if (this.containWbKey(wb.key)) {
            return WorkbookStateContainerErrors.WorkbookStateAlreadyExist.report("Can't create new workbook state for ${wb.key} because a state for such key already exist")
                .toErr()
        } else {
            val newWbState = wbStateFactory.create(ms(wb))
            return Ok(this.addOrOverwriteWbState(newWbState))
        }
    }
}
