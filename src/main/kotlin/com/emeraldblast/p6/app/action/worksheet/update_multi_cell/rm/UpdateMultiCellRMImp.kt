package com.emeraldblast.p6.app.action.worksheet.update_multi_cell.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.ResultUtils.toOk
import com.emeraldblast.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellMultiUpdateRequest
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellMultiUpdateResponse
import com.emeraldblast.p6.app.action.cell.cell_multi_update.CellUpdateEntry
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.cell.d.CellContent
import com.emeraldblast.p6.app.document.cell.d.CellValue
import com.emeraldblast.p6.app.document.cell.d.CellContentImp
import com.emeraldblast.p6.di.state.app_state.TranslatorContainerMs
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.TranslatorContainer
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapBoth
import javax.inject.Inject

class UpdateMultiCellRMImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    @TranslatorContainerMs val translatorContainerMs: Ms<TranslatorContainer>
) : UpdateMultiCellRM {
    var translatorCont by translatorContainerMs
    private var appState by appStateMs
    override fun cellMultiUpdate(request: CellMultiUpdateRequest): CellMultiUpdateResponse? {
        val req = request
        val rs = appState.getWorkbookRs(req.wbKey).andThen { wb->
            wb.getWsRs(req.wsName).andThen { ws->
                var newWs = ws
                val translator = translatorCont.getTranslatorOrCreate(ws.id)
                for(entry: CellUpdateEntry in req.cellUpdateList){
                    val cellRs = ws.getCellOrDefaultRs(entry.cellAddress)
                    if(cellRs is Ok){
                        val newCellValueContent = makeContent(entry,translator)
                        val newCell = cellRs.value.setContent(newCellValueContent)
                        newWs = newWs.addOrOverwrite(newCell)
                    }
                }
                val newWb = wb.addSheetOrOverwrite(newWs)
                newWb.toOk()
            }
        }
        val rt = rs.mapBoth(
            success = {
                CellMultiUpdateResponse(
                    WorkbookUpdateCommonResponse(
                        wbKey= req.wbKey,
                        newWorkbook = it,
                    )
                )
            },
            failure = {
                CellMultiUpdateResponse(
                    WorkbookUpdateCommonResponse(
                        wbKey= req.wbKey,
                        errorReport = it
                    )
                )
            }
        )
        return rt
    }

    fun makeContent(entry: CellUpdateEntry, translator:P6Translator<ExUnit>):CellContent{
        val formula=entry.cellUpdateContent.formula
        if(formula.isNotEmpty()){
            val transRs = translator.translate(formula)
            return CellContentImp.fromTransRs(transRs,  formula)
        }else{
            return CellContentImp(
                cellValueMs = CellValue.fromAny(entry.cellUpdateContent.cellValue).toMs(),
            )
        }
    }

}
