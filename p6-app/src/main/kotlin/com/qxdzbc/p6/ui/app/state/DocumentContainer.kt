package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookGetter
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt

/**
 * A convenient bridging interface providing various functions for querying workbooks, worksheets, cells, workbook key Ms, worksheet name Ms.
 */
interface DocumentContainer : WorkbookGetter {

    val wbCont: WorkbookContainer

    /**
     * @return [WbWsSt] from an existing worksheet, null if such worksheet does not exist
     */
    fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt?

    fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>?
    fun getWbKeyStRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>>
    fun getWbKeyMs(wbKey: WorkbookKey): Ms<WorkbookKey>?
    fun getWbKeyMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>>

    fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>?
    fun getWsNameSt(wbws:WbWs): St<String>?
    fun getWsNameStRs(wbws:WbWs): Rse<St<String>>
    fun getWsNameMs(wbKey: WorkbookKey, wsName: String): Ms<String>?

    fun getWsNameSt(wbKeySt:St<WorkbookKey>, wsName: String): St<String>?
    fun getWsNameMs(wbKeySt:St<WorkbookKey>, wsName: String): Ms<String>?

    /**
     * @return [WbWsSt] from a [WbWs], null if such combination does not exist
     */
    fun getWbWsSt(wbWs: WbWs): WbWsSt?
    /**
     * @return [WbWsSt] from a [WbWs], Err if such combination does not exist
     */
    fun getWbWsStRs(wbWs: WbWs):Rse<WbWsSt>

    fun getWsRs(wbKey: WorkbookKey, wsName: String): Rse<Worksheet>
    fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Worksheet>
    fun getWsRs(wbwsSt: WbWsSt): Rse<Worksheet>
    fun getWsRs(wbws: WbWs): Rse<Worksheet>

    fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet?
    fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet?
    fun getWs(wbwsSt: WbWsSt): Worksheet?
    fun getWs(wbws: WbWs): Worksheet?
    fun getWs(wsId: WorksheetIdWithIndexPrt): Worksheet?

    fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rse<Range>
    fun getRangeRs(rangeId: RangeId): Rse<Range>
    fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?

    fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?
    fun getLazyRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rse<Range>
    fun getLazyRangeRs(wbKeySt:St<WorkbookKey> , wsNameSt:St<String> , rangeAddress: RangeAddress): Rse<Range>

    fun getCellRsOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rse<Cell>
    fun getCellRsOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rse<Cell>
    fun getCellRsOrDefault(cellId: CellIdDM): Rse<Cell>
    fun getCellRsOrDefault(cellId: CellId): Rse<Cell>

    fun getCellOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(wbws: WbWs, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(wbwsSt: WbWsSt, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(cellId: CellIdDM): Cell?
    fun getCellOrDefault(cellId: CellId): Cell?

    fun getCellMsRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rse<Ms<Cell>>
    fun getCellMsRs(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rse<Ms<Cell>>
    fun getCellMsRs(cellId: CellIdDM): Rse<Ms<Cell>>

    fun getCellMs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Ms<Cell>?
    fun getCellMs(cellIdDM: CellIdDM): Ms<Cell>?
    fun getCellMs(cellId: CellId): Ms<Cell>?

    fun getCell(cellIdDM:CellIdDM):Cell?
    fun getCell(cellId:CellId):Cell?

    fun getCellIdRs(cellIdDM:CellIdDM):Rse<CellId>
    fun getCellId(cellIdDM:CellIdDM):CellId?

    /**
     * replace a workbook with a new workbook with the same workbook key
     */
    fun replaceWb(newWb: Workbook)
}

