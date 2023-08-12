package com.qxdzbc.p6.composite_actions.worksheet.load_data

import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.document_data_layer.cell.CellContentImp
import com.qxdzbc.p6.document_data_layer.cell.IndCellImp
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.di.anvil.P6AnvilScope


import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.rpc.worksheet.msg.LoadDataRequest
import com.qxdzbc.p6.rpc.worksheet.msg.LoadType
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class LoadDataActionImp @Inject constructor(
    val stateCont:StateContainer,
    private val errorRouter: ErrorRouter,
    val translatorCont: TranslatorContainer
) : LoadDataAction {

    val sc  = stateCont
    val tc = translatorCont

    override fun loadDataRs(request: LoadDataRequest, publishErrorToUI: Boolean): Rse<Unit> {
        val getWsMsRs = sc.getWsStateRs(request)
        val rt = getWsMsRs.flatMap { wsState ->
            val wsMs = wsState.wsMs
            val translator = tc.getTranslatorOrCreate(request)
            val newDataRs = loadDataRs(wsMs.value, request, translator)
            newDataRs.onSuccess {
                wsMs.value = it
                wsState.refreshCellState()
            }
            newDataRs
        }
        rt.onFailure {
            if (publishErrorToUI) {
                errorRouter.publishToWindow(it, request.wbKey)
            }
        }
        return rt.map { Unit }
    }

    fun loadDataRs(
        ws: Worksheet,
        request: LoadDataRequest,
        translator: P6Translator<ExUnit>
    ): Rse<Worksheet> {
        var rt: Worksheet = ws
        val lt = request.loadType
        for (indCellDM:IndependentCellDM in request.ws.cells) {
            if (lt == LoadType.KEEP_OLD_DATA_IF_COLLIDE) {
                // x: don't overwrite existing old cell
                val oldCell = rt.getCell(indCellDM.address)
                if (oldCell != null) {
                    continue
                }
            }
            val newCell = IndCellImp(
                address = indCellDM.address,
                content = indCellDM.formula?.let {
                    CellContentImp.fromTransRs(translator.translate(it),originalFormula=it)
                } ?: CellContentImp(cellValueMs = indCellDM.value.toMs(), originalText = indCellDM.content.originalText)
            )
            rt.addOrOverwrite(newCell)
        }
        return Ok(rt)
    }
}
