package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
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
 * A utility interface providing functions for querying documents' content
 */
interface DocumentContainer : WorkbookGetter {

    val wbContMs: Ms<WorkbookContainer>
    var wbCont: WorkbookContainer

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

    fun getWsRs(wbKey: WorkbookKey, wsName: String): Rs<Worksheet, ErrorReport>
    fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Worksheet, ErrorReport>
    fun getWsRs(wbwsSt: WbWsSt): Rs<Worksheet, ErrorReport>
    fun getWsRs(wbws: WbWs): Rs<Worksheet, ErrorReport>

    fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet?
    fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet?
    fun getWs(wbwsSt: WbWsSt): Worksheet?
    fun getWs(wbws: WbWs): Worksheet?
    fun getWs(wsId: WorksheetIdWithIndexPrt): Worksheet?

    fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rs<Ms<Worksheet>, ErrorReport>
    fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Ms<Worksheet>, ErrorReport>
    fun getWsMsRs(wbwsSt:WbWsSt): Rs<Ms<Worksheet>, ErrorReport>
    fun getWsMsRs(wbws:WbWs): Rs<Ms<Worksheet>, ErrorReport>

    fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>?
    fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>?
    fun getWsMs(wbws: WbWs): Ms<Worksheet>?
    fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>?

    fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>
    fun getRangeRs(rangeId: RangeId): Rs<Range, ErrorReport>
    fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?

    fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?
    fun getLazyRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>
    fun getLazyRangeRs(wbKeySt:St<WorkbookKey> , wsNameSt:St<String> , rangeAddress: RangeAddress): Rs<Range, ErrorReport>

    fun getCellRsOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Cell, ErrorReport>
    fun getCellRsOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rs<Cell, ErrorReport>
    fun getCellRsOrDefault(cellId: CellIdDM): Rs<Cell, ErrorReport>
    fun getCellRsOrDefault(cellId: CellId): Rs<Cell, ErrorReport>

    fun getCellOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(wbws: WbWs, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(wbwsSt: WbWsSt, cellAddress: CellAddress): Cell?
    fun getCellOrDefault(cellId: CellIdDM): Cell?
    fun getCellOrDefault(cellId: CellId): Cell?

    fun getCellMsRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Ms<Cell>, ErrorReport>
    fun getCellMsRs(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rs<Ms<Cell>, ErrorReport>
    fun getCellMsRs(cellId: CellIdDM): Rs<Ms<Cell>, ErrorReport>

    fun getCellMs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Ms<Cell>?
    fun getCellMs(cellId: CellIdDM): Ms<Cell>?

    /**
     * replace a workbook with a new workbook with the same workbook key
     */
    fun replaceWb(newWb: Workbook): DocumentContainer
}

