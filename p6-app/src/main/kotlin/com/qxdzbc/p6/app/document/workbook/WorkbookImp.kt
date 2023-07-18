package com.qxdzbc.p6.app.document.workbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.qxdzbc.p6.app.common.utils.WorkbookUtils
import com.qxdzbc.p6.app.document.workbook.Workbooks.isLegalWbName
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

data class WorkbookImp(
    override val keyMs: Ms<WorkbookKey>,
    val worksheetMsMapMs: Ms<Map<Ms<String>, Ms<Worksheet>>> = ms(emptyMap())
) : BaseWorkbook() {

    constructor(keyMs: Ms<WorkbookKey>, worksheetMsList: List<Ms<Worksheet>>) : this(
        keyMs,
        ms(worksheetMsList.associateBy { it.value.nameMs })
    )

    override val worksheetMsMap: Map<Ms<String>, Ms<Worksheet>> by worksheetMsMapMs

    override val worksheetMsList: List<Ms<Worksheet>> get() = worksheetMsMap.values.toList()

    override var key: WorkbookKey by keyMs

    override val worksheets: List<Worksheet> get() = worksheetMsList.map { it.value }

    override fun reRunAndRefreshDisplayText() {
        this.worksheets.forEach { it.reRunAndRefreshDisplayText() }

    }

    /**
     * justification for not returning a copy: the computed cell values are not part of the workbook state, they are derived value.
     */
    override fun reRun() {
        this.worksheets.forEach { it.reRun() }

    }

    override fun refreshDisplayText() {
        this.worksheets.forEach { it.refreshDisplayText() }

    }

    override fun toProto(): WorkbookProto {
        return WorkbookProto.newBuilder()
            .setWbKey(this.key.toProto())
            .addAllWorksheet(this.worksheets.map { it.toProto() })
            .build()
    }

    override fun makeSavableCopy(): WorkbookImp {
        return this.copy(keyMs = ms(WorkbookKey("", null)))
    }

    override val worksheetMap: Map<String, Worksheet> get() = worksheets.associateBy { it.name }

    override fun createNewWs(name: String?) {
        this.createNewWsRs(name)
    }

    override fun createNewWsWithMoreDetail(name: String?): CreateNewWorksheetResponse? {
        val rs = this.createNewWsWithMoreDetailRs(name)
        return rs.component1()
    }

    override fun createNewWsWithMoreDetailRs(name: String?): Rse<CreateNewWorksheetResponse> {
        val actualName = name ?: WorkbookUtils.generateNewSheetName(this.worksheets.map { it.name })
        if (actualName in this.worksheetMap.keys) {
            return WorkbookErrors.WorksheetAlreadyExist.report(actualName).toErr()
        } else {
            val wsMs: Ms<Worksheet> = WorksheetImp(
                nameMs = ms(actualName),
                wbKeySt = this.keyMs
            ).toMs()
            worksheetMsMapMs.value = this.worksheetMsMap + (wsMs.value.nameMs to wsMs)

            return Ok(CreateNewWorksheetResponse(this, actualName))
        }
    }

    override fun removeAllWs() {
        worksheetMsMapMs.value = emptyMap()

    }

    override fun createNewWsRs(name: String?): Rse<Unit> {
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
            worksheetMsMapMs.value = this.worksheetMsMap + (wsMs.value.nameMs to wsMs)
            return Ok(Unit)
        }
    }

    override fun removeSheet(index: Int) {
        removeSheetRs(index)
    }

    override fun removeSheet(name: String) {
        removeSheetRs(name)
    }

    override fun removeSheetRs(index: Int): Rse<Unit> {
        if (index in worksheets.indices) {
            val target = worksheetMsList[index]
            worksheetMsMapMs.value = worksheetMsMap - target.value.nameMs
            return Ok(Unit)
        } else {
            return WorkbookErrors.InvalidWorksheet.reportWithDetail("Can't delete worksheet at index \'$index\' because it does not exist within workbook at ${this.key}")
                .toErr()
        }
    }

    override fun removeSheetRs(name: String): Rse<Unit> {
        if (this.containSheet(name)) {
            worksheetMsMapMs.value = worksheetMsMap.filter { it.key.value != name }
            return Ok(Unit)
        } else {
            return WorkbookErrors.InvalidWorksheet.reportWithDetail("Can't delete worksheet named \"$name\" because it does not exist within workbook at ${this.key}")
                .toErr()
        }
    }


    override fun addWsRs(ws: Worksheet): Rse<Unit> {
        val wsName = ws.name
        if (this.containSheet(wsName)) {
            return WorkbookErrors.WorksheetAlreadyExist.report2("Can't add worksheet ${wsName} because another worksheet having the same name already exist in workbook ${this.key}.")
                .toErr()
        } else {
            return Ok(this.addSheetOrOverwrite(ws))
        }
    }

    override fun addSheetOrOverwrite(worksheet: Worksheet) {
        worksheet.setWbKeySt(this.keyMs)
        val wsMs = this.getWsMs(worksheet.name)
        if (wsMs != null) {
            wsMs.value = worksheet
        } else {
            val newWsMs = ms(worksheet)
            worksheetMsMapMs.value = worksheetMsMap + (worksheet.nameMs to newWsMs)
        }
    }

    override fun addMultiSheetOrOverwrite(worksheetList: List<Worksheet>) {
        worksheetList.forEach { ws ->
            addSheetOrOverwrite(ws)
        }
    }

    override fun renameWsRs(
        oldName: String,
        newName: String
    ): Rse<Unit> {
        val wsMs = this.getWsMs(oldName)
        if (wsMs != null) {
            if (oldName == newName) {
                return Ok(Unit)
            } else {
                if (!newName.isLegalWbName()) {
                    return WorkbookErrors.IllegalSheetName.report("Sheet name \"${newName}\" is illegal").toErr()
                }
                if (!this.containSheet(newName)) {
                    wsMs.value.nameMs.value = newName
                    return Ok(Unit)
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
    ): Rse<Unit> {
        return getWsMsRs(index).onSuccess {
            it.value.setWsName(newName)
        }.map { Unit }
    }

    override fun moveWs(fromIndex: Int, toIndex: Int): Rse<Unit> {
        TODO("Not yet implemented")
    }

    override fun moveWs(wsName: String, toIndex: Int): Rse<Unit> {
        TODO("Not yet implemented")
    }

    companion object {
        fun WorkbookProto.toShallowModel(translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>): Workbook {
            val wbKeyMs = ms(wbKey.toModel()) //shallow state
            val sheets = mutableListOf<Worksheet>()
            for (wsProto: WorksheetProto in worksheetList) {
                val nameMs = ms(wsProto.name) // shallow state
                val translator = translatorGetter(WbWsSt(wbKeyMs, nameMs))
                // shallow worksheet
                val newSheet = WorksheetImp(nameMs = nameMs, wbKeySt = wbKeyMs).apply {
                    withNewData(wsProto, translator)
                }

                sheets.add(newSheet)
            }
            return WorkbookImp(keyMs = wbKeyMs).apply {
                addMultiSheetOrOverwrite(sheets)
            }
        }

        fun random(): Workbook {
            val wb = WorkbookImp(
                keyMs = ms(WorkbookKey.random()),
                worksheetMsList = listOf(
                )
            )

            wb.addMultiSheetOrOverwrite((1..3).map {
                WorksheetImp.random()
            })
            return wb
        }
    }
}

