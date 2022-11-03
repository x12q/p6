package com.qxdzbc.p6.app.action.workbook.remove_all_ws

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RemoveAllWorksheetActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
): RemoveAllWorksheetAction {
    val sc by stateContSt
    override fun removeAllWsRs(wbKey: WorkbookKey): Rse<Unit> {
        val wbStateMsRs = sc.getWbStateMsRs(wbKey)
        val rt = wbStateMsRs.map {wbStateMs->
            val wbMs = wbStateMs.value.wbMs
            wbMs.value = wbMs.value.removeAllWs()
            wbStateMs.value = wbStateMs.value.refresh()
        }
        return rt
    }
}
