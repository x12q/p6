package com.emeraldblast.p6.app.action.cell.cell_update.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateRequest
import com.emeraldblast.p6.app.action.cell.cell_update.CellUpdateResponse
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.cell.d.CellContent
import com.emeraldblast.p6.app.document.cell.d.CellValue
import com.emeraldblast.p6.app.document.cell.d.CellContentImp
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.di.state.app_state.TranslatorContainerMs
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaVisitorFactory
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.TranslatorContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.common.compose.StateUtils.toSt
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.mapBoth
import javax.inject.Inject

class CellUpdateRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>,
    @TranslatorContainerMs val translatorContainerMs: Ms<TranslatorContainer>,
    private val translatorFactory: JvmFormulaTranslatorFactory,
    private val visitorFactory: JvmFormulaVisitorFactory,
) : CellUpdateRM {

    var appState by appStateMs
    var translatorCont by translatorContainerMs

    override fun updateCell(request: CellUpdateRequest): CellUpdateResponse {
        val wbKey = request.wbKey
        val wsName = request.wsName
        val wbRs = appState.globalWbCont.getWbRs(wbKey)

        if (wbRs is Ok) {
            val wb: Workbook = wbRs.value
            val wsMsRs = wb.getWsMsRs(request.wsName)

            if (wsMsRs is Ok) {
                val wsMs: Ms<Worksheet> = wsMsRs.value
                val ws:Worksheet = wsMs.value
                val translator: P6Translator<ExUnit> = translatorCont.getTranslatorOrCreate(ws.id)
                val newCellContent:CellContent = makeContent(request, translator)
                val updateWsRs = ws.updateCellContentRs(request.cellAddress, newCellContent)
                val rt:CellUpdateResponse = updateWsRs.mapBoth(
                    success = {
                        val newWb = wb.addSheetOrOverwrite(it)
                        CellUpdateResponse(
                            WorkbookUpdateCommonResponse(
                                wbKey = request.wbKey,
                                newWorkbook = newWb,
                            )
                        )
                    },
                    failure = {
                        CellUpdateResponse(
                            WorkbookUpdateCommonResponse(
                                wbKey = request.wbKey,
                                errorReport = it
                            )
                        )
                    }
                )
                return rt
            } else {
                return CellUpdateResponse(
                    WorkbookUpdateCommonResponse(
                        errorReport = wsMsRs.component2()
                    )
                )
            }
        } else {
            return CellUpdateResponse(
                WorkbookUpdateCommonResponse(
                    errorReport = wbRs.component2()
                )
            )
        }
    }

    private fun makeContent(request: CellUpdateRequest, translator: P6Translator<ExUnit>): CellContent {
        val formula = request.formula
        if (formula.isNullOrEmpty()) {
            return CellContentImp(
                cellValueMs= CellValue.fromAny(request.cellValue).toMs(),
            )
        } else {
            val exUnitRs = translator.translate(formula)
            return CellContentImp.fromTransRs(exUnitRs, formula)
        }
    }
}
