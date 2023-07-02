package com.qxdzbc.p6.app.action.worksheet.update_multi_cell.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.cell.multi_cell_update.UpdateMultiCellRequest
import com.qxdzbc.p6.app.action.cell.multi_cell_update.MultiCellUpdateResponse

import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.common.compose.StateUtils.toMs
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapBoth
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class UpdateMultiCellRMImp @Inject constructor(
    private val docCont: DocumentContainer,
    val translatorContainer: TranslatorContainer
) : UpdateMultiCellRM {

    var translatorCont = translatorContainer
    private val dc = docCont

    override fun cellMultiUpdate(request: UpdateMultiCellRequest): MultiCellUpdateResponse? {
        val req = request
        val rs = dc.getWbRs(req.wbKey).andThen { wb->
            wb.getWsRs(req.wsName).andThen { ws->
                var newWs = ws
                val translator = translatorCont.getTranslatorOrCreate(ws.id)
                for(entry in req.cellUpdateList){
                    val cellRs = ws.getCellOrDefaultRs(entry.address)
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
                MultiCellUpdateResponse(
                    WorkbookUpdateCommonResponse(
                        wbKey= req.wbKey,
                        newWorkbook = it,
                    )
                )
            },
            failure = {
                MultiCellUpdateResponse(
                    WorkbookUpdateCommonResponse(
                        wbKey= req.wbKey,
                        errorReport = it
                    )
                )
            }
        )
        return rt
    }

    private fun makeContent(indCellDM: IndependentCellDM, translator:P6Translator<ExUnit>): CellContent {
        val formula=indCellDM.content.formula
        if(!formula.isNullOrEmpty()) {
            val transRs = translator.translate(formula)
            return CellContentImp.fromTransRs(transRs,formula)
        }else{
            return CellContentImp(
                cellValueMs = CellValue.fromAny(indCellDM.content.cellValue).toMs(),
                originalText= indCellDM.content.originalText
            )
        }
    }

}
