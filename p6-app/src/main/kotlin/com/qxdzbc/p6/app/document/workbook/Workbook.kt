package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.common.utils.WithSize
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Result

/**
 * essentially a map of worksheet
 */
interface Workbook : WithSize {

    fun reRun():Workbook

    fun toProto(): WorkbookProto

    fun makeSavableCopy(): Workbook

    val keyMs:Ms<WorkbookKey>
    var key: WorkbookKey

    val worksheetMapMs:Map<Ms<String>,Ms<Worksheet>>
    val worksheetMsList: List<Ms<Worksheet>>
    val worksheetMap: Map<String, Worksheet>
    val worksheets: List<Worksheet>
    override val size: Int get() = worksheetMap.size

    fun getWsMs(index: Int): Ms<Worksheet>?
    fun getWsMsRs(index: Int): Rse<Ms<Worksheet>>
    fun getWs(index: Int): Worksheet?

    fun getWsRs(index: Int): Result<Worksheet, ErrorReport>

    fun getWsMs(name: String): Ms<Worksheet>?
    fun getWsMsRs(name: String): Rse<Ms<Worksheet>>
    fun getWs(name: String): Worksheet?
    fun getWsRs(name: String): Result<Worksheet, ErrorReport>

    /**
     * trying to create a new worksheet inside this workbook.
     * @throws Exception if unable to create a new worksheet
     */
    fun createNewWs(name: String? = null): Workbook
    fun createNewWs2(name: String? = null): CreateNewWorksheetResponse2
    fun createNewWsRs(name: String? = null): Result<Workbook, ErrorReport>

    fun removeSheet(index: Int): Workbook
    fun removeSheet(name: String): Workbook

    fun removeSheetRs(index: Int): Rse<Workbook>
    fun removeSheetRs(name: String): Rse<Workbook>

    fun addWsRs(ws: Worksheet): Rse<Workbook>

    fun addSheetOrOverwrite(worksheet: Worksheet): Workbook
    fun addMultiSheetOrOverwrite(worksheetList: List<Worksheet>): Workbook

    /**
     * for renaming a worksheet inside a workbook. This include checking the legality of the new worksheet name (illegal name format, name collision)
     */
    fun renameWsRs(oldName: String, newName: String,
                   translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>
    ): Result<Workbook, ErrorReport>
    fun renameWsRs(index: Int, newName: String,
                   translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>
    ): Result<Workbook, ErrorReport>

    fun moveWs(targetIndex: Int, toIndex: Int): Result<Workbook, ErrorReport>
    fun moveWs(targetName: String, toIndex: Int): Result<Workbook, ErrorReport>

    fun setKey(newKey: WorkbookKey): Workbook
    fun containSheet(sheetName: String): Boolean {
        return this.getWs(sheetName) != null
    }

    fun createNewWorksheetRs2(name: String?): Rse<CreateNewWorksheetResponse2>
}
