package com.qxdzbc.p6.app.action.worksheet.remove_all_cell

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RemoveAllCellActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
) : RemoveAllCellAction {
    val sc by stateContSt
    override fun removeAllCell(wbWs: WbWs): Rse<Unit> {
        val wsStateMsRs = sc.getWsStateMsRs(wbWs)
        val o:Rse<Unit> = wsStateMsRs.map {wsStateMs->
            val wsMs = wsStateMs.value.wsMs
            wsMs.value = wsMs.value.removeAllCell()
            wsStateMs.value = wsStateMs.value.refreshCellState()
        }
        return o
    }
}
