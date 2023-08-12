package com.qxdzbc.p6.composite_actions.workbook.remove_all_ws

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class RemoveAllWorksheetActionImp @Inject constructor(
    private val stateCont:StateContainer,
): RemoveAllWorksheetAction {
    val sc  = stateCont
    override fun removeAllWsRs(wbKey: WorkbookKey): Rse<Unit> {
        val wbStateMsRs = sc.getWbStateRs(wbKey)
        val rt = wbStateMsRs.map {wbStateMs->
            val wbMs = wbStateMs.wb
            wbMs.removeAllWs()
            wbStateMs.refresh()
        }
        return rt
    }
}
