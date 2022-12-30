package com.qxdzbc.p6.app.action.cell.cell_update

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.onFailure
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell.CellRM
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.paste_range.RangeCopyDM
import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.di.anvil.P6AnvilScope


import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@ContributesBinding(P6AnvilScope::class)
class UpdateCellActionImp @Inject constructor(
    private val cellRM: CellRM,
    val scSt:St<@JvmSuppressWildcards StateContainer>,
    val translatorContainerMs: St<@JvmSuppressWildcards TranslatorContainer>,
    val errorRouter: ErrorRouter,
) : UpdateCellAction {
    val sc by scSt
    val translatorCont by translatorContainerMs

    override fun updateCellDM(request: CellUpdateRequestDM, publishError:Boolean): Rse<Unit> {
        val cellRs = sc.getCellRsOrDefault(request.cellId)
        return cellRs.flatMap { cell->
            updateCell(CellUpdateRequest(
                cell.id,request.cellContent
            ),publishError)
        }
    }

    override fun updateCell(request: CellUpdateRequest, publishError: Boolean): Rse<Unit> {
        val getWsMsRs = sc.getWsStateMsRs(request)
        val rt = getWsMsRs.flatMap {wsStateMs->
            val wsMs = wsStateMs.value.wsMs
            val ws by wsMs
            val wbMs = sc.getWbMs(ws.wbKeySt)
            val translator: P6Translator<ExUnit> = translatorCont.getTranslatorOrCreate(ws.id)
            val content: CellContent = request.cellContentDM.toCellContent(translator)
            val updateWsRs = ws.updateCellContentRs(
                request.cellId.address, content
            )

            updateWsRs.flatMap {
                wsMs.value = it
                wsStateMs.value = wsStateMs.value.refreshCellState()
                if(wbMs!=null){
                    /*
                    the target ws belongs to a valid workbook, therefore, need
                    to refresh the whole app
                     */
                    sc.wbCont.allWbMs.forEach { wbMs->
                        wbMs.value = wbMs.value.reRun().refreshDisplayText()
                    }
                }else{
                    /*
                    the target ws does not belong to any valid workbook, just need
                    to refresh itself.
                     */
                    wsMs.value = ws.reRunAndRefreshDisplayText()
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
