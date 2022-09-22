package com.qxdzbc.p6.app.action.cell.cell_update

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.onFailure
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.di.state.app_state.TranslatorContainerSt
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import javax.inject.Inject

class UpdateCellActionImp @Inject constructor(
    private val cellRM: CellRM,
    @StateContainerSt
    val scSt:St<@JvmSuppressWildcards StateContainer>,
    @TranslatorContainerSt
    val translatorContainerMs: St<@JvmSuppressWildcards TranslatorContainer>,
    val errorRouter: ErrorRouter,
) : UpdateCellAction {
    val sc by scSt
    val translatorCont by translatorContainerMs

    override fun updateCell2(request: CellUpdateRequest, publishError:Boolean): Rse<Unit> {
        val getWsMsRs = sc.getWsStateMsRs(request)
        val rt = getWsMsRs.flatMap {wsStateMs->
            val wsMs = wsStateMs.value.wsMs
            val ws by wsMs
            val wbMs = sc.getWbMs(ws.wbKeySt)
            val translator: P6Translator<ExUnit> = translatorCont.getTranslatorOrCreate(ws.id)
            val content = request.cellContent.toStateObj(translator)
            val updateWsRs = ws.updateCellContentRs(
                request.cellAddress, content
            )
            updateWsRs.flatMap {
                wsMs.value = it
                wsStateMs.value = wsStateMs.value.refreshCellState()
                if(wbMs!=null){
                    wbMs.value = wbMs.value.reRun()
                }else{
                    wsMs.value = wsMs.value.reRun()
                }
                Unit.toOk()
            }
        }.onFailure {
            if(publishError){
                errorRouter.publishToWindow(it,request.wbKey)
            }
        }
        return rt
    }
}
