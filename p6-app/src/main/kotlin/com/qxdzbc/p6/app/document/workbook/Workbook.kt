package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.St

/**
 * essentially a map of worksheet
 */
interface Workbook : WithSize {

    fun reRun():Workbook
    fun refreshDisplayText():Workbook

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
    fun getWsMs(nameSt: St<String>): Ms<Worksheet>?
    fun getWsMsRs(name: String): Rse<Ms<Worksheet>>
    fun getWsMsRs(nameSt: St<String>): Rse<Ms<Worksheet>>

    fun getWs(name: String): Worksheet?
    fun getWs(nameSt: St<String>): Worksheet?
    fun getWsRs(name: String): Result<Worksheet, ErrorReport>
    fun getWsRs(nameSt: St<String>): Result<Worksheet, ErrorReport>

    /**
     * trying to create a new worksheet inside this workbook.
     * @throws Exception if unable to create a new worksheet
     */
    fun createNewWs(name: String? = null): Workbook
    fun createNewWs_MoreDetail(name: String? = null): CreateNewWorksheetResponse
    fun createNewWsRs(name: String? = null): Result<Workbook, ErrorReport>

    /**
     * create a new ws, return a more detailed result than [createNewWsRs]
     */
    fun createNewWsRs_MoreDetail(name: String?): Rse<CreateNewWorksheetResponse>

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
    fun renameWsRs(oldName: String, newName: String): Result<Workbook, ErrorReport>
    fun renameWsRs(index: Int, newName: String): Result<Workbook, ErrorReport>

    fun moveWs(targetIndex: Int, toIndex: Int): Result<Workbook, ErrorReport>
    fun moveWs(targetName: String, toIndex: Int): Result<Workbook, ErrorReport>

    fun setKey(newKey: WorkbookKey): Workbook
    fun containSheet(sheetName: String): Boolean {
        return this.getWs(sheetName) != null
    }

    fun removeAllWs(): Workbook
}
