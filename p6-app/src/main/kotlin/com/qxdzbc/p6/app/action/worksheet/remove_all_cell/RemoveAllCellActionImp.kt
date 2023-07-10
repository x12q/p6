package com.qxdzbc.p6.app.action.worksheet.remove_all_cell

import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RemoveAllCellActionImp @Inject constructor(
    private val stateCont:StateContainer,
) : RemoveAllCellAction {

    val sc  = stateCont

    fun removeAllCell(wsStateMs: Ms<WorksheetState>) {
        val wsMs = wsStateMs.value.wsMs
        wsMs.value = wsMs.value.removeAllCell()
        wsStateMs.value.refreshCellState()
    }

    override fun removeAllCell(wbWsSt: WbWsSt): Rse<Unit> {
        val o: Rse<Unit> = sc.getWsStateMsRs(wbWsSt).map { wsStateMs ->
            removeAllCell(wsStateMs)
        }
        return o
    }

    override fun removeAllCell(wbWs: WbWs): Rse<Unit> {
        val o: Rse<Unit> = sc.getWsStateMsRs(wbWs).map { wsStateMs ->
            removeAllCell(wsStateMs)
        }
        return o
    }
}
