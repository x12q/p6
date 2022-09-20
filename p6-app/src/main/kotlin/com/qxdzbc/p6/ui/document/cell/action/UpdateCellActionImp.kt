package com.qxdzbc.p6.ui.document.cell.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.cell_update.applier.CellUpdateApplier
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse
import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.di.state.app_state.TranslatorContainerMs
import com.qxdzbc.p6.di.state.app_state.TranslatorContainerSt
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import javax.inject.Inject

class UpdateCellActionImp @Inject constructor(
    private val cellRM: CellRM,
    private val cellUpdateApplier: CellUpdateApplier,
    @StateContainerSt
    val scSt:St<@JvmSuppressWildcards StateContainer>,
    @TranslatorContainerSt
    val translatorContainerMs: St<@JvmSuppressWildcards TranslatorContainer>,
) : UpdateCellAction {
    val sc by scSt
    val translatorCont by translatorContainerMs
    override fun updateCell(request: CellUpdateRequest):Rse<Unit> {
        val response: CellUpdateResponse? = cellRM.updateCell(request)
        if (response != null) {
            cellUpdateApplier.applyRes(response)
        }
        return Ok(Unit)
    }

    override fun updateCell2(request: CellUpdateRequest2): Rse<Unit> {
        val getWsMsRs = sc.getWsStateMsRs(request)
        val rt = getWsMsRs.flatMap {wsStateMs->
            val wsMs = wsStateMs.value.wsMs
            val translator: P6Translator<ExUnit> = translatorCont.getTranslatorOrCreate(wsMs.value.id)
            val content = request.cellContent.toStateObj(translator)
            val updateWsRs = wsMs.value.updateCellContentRs(
                request.cellAddress, content
            )
            updateWsRs.flatMap {
                wsMs.value = it
                wsStateMs.value = wsStateMs.value.refreshCellState()
                Unit.toOk()
            }
        }
        return rt
    }
}
