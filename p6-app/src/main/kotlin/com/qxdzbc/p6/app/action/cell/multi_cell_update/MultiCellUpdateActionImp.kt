package com.qxdzbc.p6.app.action.cell.multi_cell_update

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.AppCoroutineScope

import com.qxdzbc.p6.di.state.app_state.TranslatorContainerSt
import com.qxdzbc.p6.rpc.common_data_structure.IndCellDM
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MultiCellUpdateActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    @TranslatorContainerSt
    val tcSt: St<@JvmSuppressWildcards TranslatorContainer>,
    val errorRouter: ErrorRouter,
    @AppCoroutineScope
    val crtScope: CoroutineScope
) : MultiCellUpdateAction {
    val sc by stateContSt
    val tc by tcSt

    override fun updateMultiCellDM(request: MultiCellUpdateRequestDM, publishErr: Boolean): Rse<Unit> {
        val wsStateMsRs = sc.getWsStateMsRs(request)
        val rt = updateMultiCell(wsStateMsRs,request.cellUpdateList)
        rt.onFailure {
            if (publishErr) {
                errorRouter.publishToWindow(it, request.wbKey)
            }
        }
        return rt
    }

    override fun updateMultiCell(request: MultiCellUpdateRequest, publishErr: Boolean): Rse<Unit> {
        val wsStateMsRs = sc.getWsStateMsRs(request)
        val rt = this.updateMultiCell(wsStateMsRs,request.cellUpdateList)
        rt.onFailure {
            if (publishErr) {
                errorRouter.publishToWindow(it, request.wbKey)
            }
        }
        return rt
    }

    fun updateMultiCell(wsStateMsRs:Rse<Ms<WorksheetState>>, cellUpdateList:List<IndCellDM>): Rse<Unit> {
        val rt = wsStateMsRs.flatMap { wsStateMs ->
            var ws = wsStateMs.value.worksheet
            var err: Err<ErrorReport>? = null
            val translator = tc.getTranslatorOrCreate(
                wbKeySt = ws.wbKeySt, wsNameSt = ws.nameMs
            )
            // x: update ws with new data
            for (entry in cellUpdateList) {
                val updateRs = ws.updateCellContentRs(
                    cellAddress = entry.address,
                    cellContent = entry.content.toStateObj(translator)
                )
                updateRs.onSuccess {
                    ws = it
                }
                if (updateRs is Err) {
                    err = updateRs
                    break
                }
            }
            val noErr = err == null
            if (noErr) {
                // x: update state obj
                if (ws != wsStateMs.value.wsMs.value) {
                    wsStateMs.value.wsMs.value = ws
                    wsStateMs.value = wsStateMs.value.refresh()
                }
                sc.getWbStateMs(ws.wbKey)?.also {
                    it.value.wbMs.value = it.value.wb.reRun()
                    it.value = it.value.refresh()
                }
            }
            err ?: Ok(Unit)
        }
        return rt
    }
}
