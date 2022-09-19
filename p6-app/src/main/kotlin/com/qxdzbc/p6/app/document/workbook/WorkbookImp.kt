package com.qxdzbc.p6.app.document.workbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOr
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.p6.app.common.utils.WorkbookUtils
import com.qxdzbc.p6.app.document.workbook.Workbooks.isLegalWbName
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp.Companion.toShallowModel
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

data class WorkbookImp(
    override val keyMs: Ms<WorkbookKey>,
    override val worksheetMapMs: Map<Ms<String>, Ms<Worksheet>> = emptyMap()
) : BaseWorkbook() {

    companion object {
        fun WorkbookProto.toShallowModel(translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>): Workbook {
            val wbKeyMs = ms(wbKey.toModel()) //shallow state
            val sheets = mutableListOf<Worksheet>()
            for (wsProto: WorksheetProto in worksheetList) {
                val nameMs = ms(wsProto.name) // shallow state
                val translator = translatorGetter(WbWsSt(wbKeyMs, nameMs))
                // shallow worksheet
                val newSheet = WorksheetImp(nameMs = nameMs, wbKeySt = wbKeyMs).withNewData(wsProto, translator)
                sheets.add(newSheet)
            }
            return WorkbookImp(keyMs = wbKeyMs).addMultiSheetOrOverwrite(sheets)
        }
    }

    constructor(keyMs: Ms<WorkbookKey>, worksheetMsList: List<Ms<Worksheet>>) : this(keyMs,
        worksheetMsList.associateBy { it.value.nameMs }
    )

    override val worksheetMsList: List<Ms<Worksheet>> get() = worksheetMapMs.values.toList()
    override var key: WorkbookKey by keyMs

    override val worksheets: List<Worksheet> get() = worksheetMsList.map { it.value }

    /**
     * justification for not returning a copy: the computed cell values are not part of the workbook state, they are derived value.
     */
    override fun reRun(): Workbook {
        this.worksheets.forEach { it.reRun() }
        return this
    }

    override fun toProto(): WorkbookProto {
        return WorkbookProto.newBuilder()
            .setWbKey(this.key.toProto())
            .addAllWorksheet(this.worksheets.map { it.toProto() })
            .build()
    }

    override fun makeSavableCopy(): Workbook {
        return this.copy(keyMs = WorkbookKey("", null).toMs())
    }

    override val worksheetMap: Map<String, Worksheet> get() = worksheets.associateBy { it.name }

    @kotlin.jvm.Throws(Exception::class)
    override fun createNewWs(name: String?): Workbook {
        val rs = this.createNewWsRs(name)
        return rs.getOrThrow()
    }

    @kotlin.jvm.Throws(Exception::class)
    override fun createNewWs2(name: String?): CreateNewWorksheetResponse2 {
        val rs = this.createNewWorksheetRs2(name)
        return rs.getOrThrow()
    }

    override fun createNewWorksheetRs2(name: String?): Rse<CreateNewWorksheetResponse2> {
        val actualName = name ?: WorkbookUtils.generateNewSheetName(this.worksheets.map { it.name })
        if (actualName in this.worksheetMap.keys) {
            return WorkbookErrors.WorksheetAlreadyExist.report(actualName).toErr()
        } else {
            val wsMs: Ms<Worksheet> = WorksheetImp(
                nameMs = ms(actualName),
                wbKeySt = this.keyMs
            ).toMs()
            val newWb = (this.copy(
                worksheetMapMs = this.worksheetMapMs + (wsMs.value.nameMs to wsMs)
            ))

            return Ok(CreateNewWorksheetResponse2(newWb, actualName))
        }
    }

    override fun removeAllWs(): Workbook {
        return this.copy(worksheetMapMs = emptyMap())
    }

    override fun createNewWsRs(name: String?): Result<Workbook, ErrorReport> {
        val actualName = name ?: WorkbookUtils.generateNewSheetName(this.worksheets.map { it.name })
        if (actualName in this.worksheetMap.keys) {
            return WorkbookErrors.WorksheetAlreadyExist.report(actualName).toErr()
        } else {
            val wsMs: Ms<Worksheet> = ms(
                WorksheetImp(
                    nameMs = ms(actualName),
                    wbKeySt = this.keyMs
                )
            )
            return Ok(
                (this.copy(
                    worksheetMapMs = this.worksheetMapMs + (wsMs.value.nameMs to wsMs)
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
            return this.copy(worksheetMapMs = worksheetMapMs - target.value.nameMs).toOk()
        } else {
            return WorkbookErrors.InvalidWorksheet.reportWithDetail("Can't delete worksheet at index \'$index\' because it does not exist within workbook at ${this.key}")
                .toErr()
        }
    }

    override fun removeSheetRs(name: String): Rse<Workbook> {
        if (this.containSheet(name)) {
            return this.copy(worksheetMapMs = worksheetMapMs.filter { it.key.value != name }).toOk()
        } else {
            return WorkbookErrors.InvalidWorksheet.reportWithDetail("Can't delete worksheet named \"$name\" because it does not exist within workbook at ${this.key}")
                .toErr()
        }
    }


    override fun addWsRs(ws: Worksheet): Rse<Workbook> {
        val wsName = ws.name
        if (this.containSheet(wsName)) {
            return WorkbookErrors.WorksheetAlreadyExist.report2("Can't add worksheet ${wsName} because another worksheet having the same name already exist in workbook ${this.key}.")
                .toErr()
        } else {
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
            return this.copy(worksheetMapMs = worksheetMapMs + (newSheet.nameMs to newWsMs))
        }
    }

    override fun addMultiSheetOrOverwrite(worksheetList: List<Worksheet>): Workbook {
        return worksheetList.fold(this as Workbook) { acc, ws ->
            acc.addSheetOrOverwrite(ws)
        }
    }

    override fun renameWsRs(
        oldName: String,
        newName: String
    ): Result<Workbook, ErrorReport> {
        val wsMs = this.getWsMs(oldName)
        if (wsMs != null) {
            if (oldName == newName) {
                return Ok(this)
            } else {
                if (!newName.isLegalWbName()) {
                    return WorkbookErrors.IllegalSheetName.report("Sheet name \"${newName}\" is illegal").toErr()
                }
                if (!this.containSheet(newName)) {
                    val oldWorkSheet = wsMs.value
                    val newWorksheet = oldWorkSheet.setWsName(newName)
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
    ): Result<Workbook, ErrorReport> {
        val oldWsMs = this.getWsMs(index)

        if (oldWsMs != null) {
            val oldWorksheet = oldWsMs.value
            val newWorksheet = oldWorksheet.setWsName(newName)
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

//    override fun hashCode(): Int {
//        var result = key.hashCode()
//        result = 31 * result + worksheets.hashCode()
//        return result
//    }

//    override fun isSimilar(wb: Workbook): Boolean {
//
//        val similarKey = key == wb.key
//        val c2 = worksheets.size == wb.worksheets.size
//        val c3 = if (c2) {
//            var z = true
//            for ((i, ws) in worksheets.withIndex()) {
//                if (!ws.isSimilar(wb.worksheets[i])) {
//                    z = false
//                    break
//                }
//            }
//            z
//        } else {
//            false
//        }
//        return similarKey && c2 && c3
//
//    }

//    override fun equals(other: Any?): Boolean {
//        if (other is Workbook) {
//            val c1 = key == other.key
//            val c2 = worksheets == other.worksheets
//            return c1 && c2
//        } else {
//            return false
//        }
//    }
}

