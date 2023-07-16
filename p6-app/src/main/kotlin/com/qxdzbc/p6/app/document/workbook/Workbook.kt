package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.CanConvertToWorkbookProto

/**
 * this is essentially a map of worksheet
 */
interface Workbook : WithSize, CanConvertToWorkbookProto {

    fun reRun()
    fun refreshDisplayText()

    /**
     * A savable copy is a workbook with an empty workbook key (with empty name and null path), that is completely detached from the app state.
     */
    fun makeSavableCopy(): Workbook

    val keyMs:Ms<WorkbookKey>
    var key: WorkbookKey

    val worksheetMsMap:Map<Ms<String>,Ms<Worksheet>>
    val worksheetMsList: List<Ms<Worksheet>>
    val worksheetMap: Map<String, Worksheet>
    val worksheets: List<Worksheet>

    fun getWsMs(index: Int): Ms<Worksheet>?
    fun getWsMsRs(index: Int): Rse<Ms<Worksheet>>
    fun getWs(index: Int): Worksheet?

    fun getWsRs(index: Int): Rse<Worksheet>

    fun getWsMs(name: String): Ms<Worksheet>?
    fun getWsMs(nameSt: St<String>): Ms<Worksheet>?
    fun getWsMsRs(name: String): Rse<Ms<Worksheet>>
    fun getWsMsRs(nameSt: St<String>): Rse<Ms<Worksheet>>

    fun getWs(name: String): Worksheet?
    fun getWs(nameSt: St<String>): Worksheet?
    fun getWsRs(name: String): Rse<Worksheet>
    fun getWsRs(nameSt: St<String>): Rse<Worksheet>

    /**
     * trying to create a new worksheet inside this workbook.
     * do nothing if unable to create a new worksheet for that name
     */
    fun createNewWs(name: String? = null)

    /**
     * create a new ws, return a more info than [createNewWsRs], including:
     * - the new workbook
     * - name of the newly created worksheet
     * @return null if unable to create a new worksheet. This function swallow any errors arising during the worksheet-creating process.
     */
    fun createNewWsWithMoreDetail(name: String? = null): CreateNewWorksheetResponse?

    fun createNewWsRs(name: String? = null): Rse<Unit>

    /**
     * create a new ws, return a more info than [createNewWsRs], including:
     * - the new workbook
     * - name of the newly created worksheet
     */
    fun createNewWsWithMoreDetailRs(name: String? = null): Rse<CreateNewWorksheetResponse>

    fun removeSheet(index: Int)
    fun removeSheet(name: String)

    fun removeSheetRs(index: Int): Rse<Unit>
    fun removeSheetRs(name: String): Rse<Unit>

    fun addWsRs(ws: Worksheet): Rse<Unit>

    fun addSheetOrOverwrite(worksheet: Worksheet)
    fun addMultiSheetOrOverwrite(worksheetList: List<Worksheet>)

    /**
     * for renaming a worksheet inside a workbook. This include checking the legality of the new worksheet name (illegal name format, name collision)
     * TODO this function is not appropriate. A better way is to get the worksheet, and perform the change on the worksheet itself.
     */
    fun renameWsRs(oldName: String, newName: String): Rse<Unit>
    fun renameWsRs(index: Int, newName: String): Rse<Unit>

    /**
     * Move worksheet at [fromIndex] to [toIndex]
     */
    fun moveWs(fromIndex: Int, toIndex: Int): Rse<Unit>

    /**
     * Move worksheet with name == [wsName] to [toIndex]
     */
    fun moveWs(wsName: String, toIndex: Int): Rse<Unit>

    /**
     * @return true if this workbook contains a worksheet with name == [wsName]
     */
    fun containSheet(wsName: String): Boolean {
        return this.getWs(wsName) != null
    }

    /**
     * Remove all worksheet from this workbook
     */
    fun removeAllWs()

    /**
     * rerun all formula and refresh display text of all worksheet in this workbook.
     */
    fun reRunAndRefreshDisplayText()

    companion object{
        fun random():Workbook{
            val wb=WorkbookImp(
                keyMs= ms(WorkbookKey.random()),
                worksheetMsList = listOf(
                )
            )

            wb.addMultiSheetOrOverwrite((1 .. 3).map{
                Worksheet.random()
            })
            return wb
        }
    }
}
