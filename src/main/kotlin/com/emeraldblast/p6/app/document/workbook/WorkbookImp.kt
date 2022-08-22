package com.emeraldblast.p6.app.document.workbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.app.common.utils.WorkbookUtils
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.emeraldblast.p6.app.common.utils.ResultUtils.toOk
import com.emeraldblast.p6.app.document.workbook.Workbooks.isLegalWbName
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.app.document.worksheet.WorksheetImp
import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.DocProtos.WorkbookProto
import com.emeraldblast.p6.proto.DocProtos.WorksheetProto
import com.emeraldblast.p6.translator.P6Translator
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms
import com.github.michaelbull.result.*
import kotlin.collections.fold

data class WorkbookImp(
    override val keyMs: Ms<WorkbookKey>,
    override val worksheetMsList: List<Ms<Worksheet>> = emptyList(),
) : BaseWorkbook() {

    override var key: WorkbookKey by keyMs
    override val worksheetMapMs: Map<Ms<String>, Ms<Worksheet>>
        get() = worksheetMsList.associateBy { it.value.nameMs }
    override val worksheets: List<Worksheet> get() = worksheetMsList.map { it.value }

    companion object {
        fun WorkbookProto.toModel(translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>): Workbook {
            val wbKey = workbookKey.toModel()
            val wbKeyMs = ms(wbKey)
            val sheets = mutableListOf<Worksheet>()
            for (sheet: WorksheetProto in worksheetList) {
                val nameMs = ms(sheet.name)
                val translator = translatorGetter(WbWsSt(wbKeyMs,nameMs))
                val newSheet = WorksheetImp(nameMs = nameMs, wbKeySt = wbKeyMs).withNewData(sheet, translator)
                sheets.add(newSheet)
            }
            return WorkbookImp(keyMs = wbKey.toMs()).addMultiSheetOrOverwrite(sheets)
        }
    }

    override fun getWsMs(index: Int): Ms<Worksheet>? {
        return worksheetMsList.getOrNull(index)
    }

    override fun getWsMs(name: String): Ms<Worksheet>? {
        return worksheetMsList.firstOrNull { it.value.name == name }
    }

    override fun getWsMsRs(index: Int): Rse<Ms<Worksheet>> {
        val ws = getWsMs(index)
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(index).toErr()
    }

    override fun getWsMsRs(name: String): Rse<Ms<Worksheet>> {
        val ws = getWsMs(name)
        return ws?.let { Ok(it) } ?: WorkbookErrors.InvalidWorksheet.report(name).toErr()
    }


    /**
     * justification for not returning a copy: the computed cell values are not part of the workbook state, they are derived value.
     */
    override fun reRun(): Workbook {
        this.worksheets.forEach { it.reRun() }
        return this
    }

    override fun toProto(): WorkbookProto {
        return WorkbookProto.newBuilder()
            .setWorkbookKey(this.key.toProto())
            .addAllWorksheet(this.worksheets.map { it.toProto() })
            .build()
    }

    override fun makeSavableCopy(): Workbook {
        return this.copy(keyMs = WorkbookKey("", null).toMs())
    }

    override val worksheetMap: Map<String, Worksheet> get() = worksheets.associateBy { it.name }

    override fun createNewWs(name: String?): Workbook {
        val rs = this.createNewWsRs(name)
        return rs.getOrThrow()
    }

    override fun createNewWs2(name: String?): CreateNewWorksheetResponse2 {
        val rs = this.createNewWorksheetRs2(name)
        return rs.getOrThrow()
    }

    override fun createNewWorksheetRs2(name: String?): Rse<CreateNewWorksheetResponse2> {
        val actualName = name ?: WorkbookUtils.generateNewSheetName(this.worksheets.map { it.name })
        if (actualName in this.worksheetMap.keys) {
            return WorkbookErrors.WorksheetAlreadyExist.report(actualName).toErr()
        } else {
            val newWb = (this.copy(
                worksheetMsList = this.worksheetMsList + WorksheetImp(
                    nameMs = ms(actualName),
                    wbKeySt = this.keyMs
                ).toMs()
            ))

            return Ok(CreateNewWorksheetResponse2(newWb, actualName))
        }
    }

    override fun createNewWsRs(name: String?): Result<Workbook, ErrorReport> {
        val actualName = name ?: WorkbookUtils.generateNewSheetName(this.worksheets.map { it.name })
        if (actualName in this.worksheetMap.keys) {
            return WorkbookErrors.WorksheetAlreadyExist.report(actualName).toErr()
        } else {
            return Ok(
                (this.copy(
                    worksheetMsList = this.worksheetMsList + ms(
                        WorksheetImp(
                            nameMs = ms(actualName),
                            wbKeySt = this.keyMs
                        )
                    )
                ))
            )
        }
    }

    override fun removeSheet(index: Int): Workbook {
        return removeSheetRs(index).getOr(this)
    }

    override fun removeSheet(name: String): Workbook {
        return removeSheetRs(name).getOr(this)
    }

    override fun removeSheetRs(index: Int): Rse<Workbook> {
        if (index in worksheets.indices) {
            val target = worksheetMsList[index]
            return this.copy(worksheetMsList = worksheetMsList - target).toOk()
        } else {
            return WorkbookErrors.InvalidWorksheet.reportWithDetail("Can't delete worksheet at index \'$index\' because it does not exist within workbook at ${this.key}").toErr()
        }
    }

    override fun removeSheetRs(name: String): Rse<Workbook> {
        if(this.containSheet(name)){
            return this.copy(worksheetMsList = this.worksheetMsList.filter { it.value.name != name }).toOk()
        }else{
            return WorkbookErrors.InvalidWorksheet.reportWithDetail("Can't delete worksheet named \"$name\" because it does not exist within workbook at ${this.key}").toErr()
        }
    }


    override fun addWsRs(ws: Worksheet): Rse<Workbook> {
        val wsName = ws.name
        if(this.containSheet(wsName)){
            return WorkbookErrors.WorksheetAlreadyExist.report2("Can't add worksheet ${wsName} because another worksheet having the same name already exist in workbook ${this.key}.").toErr()
        }else{
            return Ok(this.addSheetOrOverwrite(ws))
        }
    }

    override fun addSheetOrOverwrite(worksheet: Worksheet): Workbook {
        val newSheet = worksheet.setWbKeySt(this.keyMs)
        val wsMs = this.getWsMs(worksheet.name)
        if (wsMs != null) {
            wsMs.value = newSheet
            return this
        } else {
            val newWsMs = ms(newSheet)
            return this.copy(worksheetMsList = worksheetMsList + newWsMs)
        }
    }

    override fun addMultiSheetOrOverwrite(worksheetList: List<Worksheet>): Workbook {
        return worksheetList.fold(this as Workbook) { acc, ws ->
            acc.addSheetOrOverwrite(ws)
        }
    }

    override fun renameWsRs(
        oldName: String,
        newName: String,
        translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>
    ): Result<Workbook, ErrorReport> {
        val wsMs = this.getWsMs(oldName)
        if (wsMs!=null) {
            if (oldName == newName) {
                return Ok(this)
            } else {
                if (!newName.isLegalWbName()) {
                    return WorkbookErrors.IllegalSheetName.report("Sheet name \"${newName}\" is illegal").toErr()
                }
                if (!this.containSheet(newName)) {
                    val oldWorkSheet = wsMs.value
                    val newWorksheet = oldWorkSheet.setWsName(newName, translatorGetter(oldWorkSheet.id))
                    wsMs.value = newWorksheet
                    return Ok(this)
                } else {
                    return WorkbookErrors.WorksheetAlreadyExist.report(newName).toErr()
                }
            }
        } else {
            return WorkbookErrors.InvalidWorksheet.report(oldName).toErr()
        }
    }

    override fun renameWsRs(
        index: Int,
        newName: String,
        translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>
    ): Result<Workbook, ErrorReport> {
        val oldWsMs = this.getWsMs(index)

        if (oldWsMs != null) {
            val oldWorksheet = oldWsMs.value
            val newWorksheet = oldWorksheet.setWsName(newName, translatorGetter(oldWorksheet.id))
            oldWsMs.value = newWorksheet
            return Ok(this)
        } else {
            return Err(ErrorReport(ErrorHeader("zx", "${index} sheet does not exist"), ""))
        }
    }

    override fun moveWs(targetIndex: Int, toIndex: Int): Result<Workbook, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun moveWs(targetName: String, toIndex: Int): Result<Workbook, ErrorReport> {
        TODO("Not yet implemented")
    }

    override fun setKey(newKey: WorkbookKey): Workbook {
        this.keyMs.value = newKey
        return this
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + worksheets.hashCode()
        return result
    }
    override fun equals(other: Any?): Boolean {
        if(other is Workbook){
            val c1 = key == other.key
            val c2 = worksheets == other.worksheets
            return c1 && c2
        }else{
            return false
        }
    }
}

