package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.Rs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookGetter
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt

/**
 * A utility interface providing functions for querying documents' content
 */
interface DocumentContainer : WorkbookGetter {

    val globalWbContMs: Ms<WorkbookContainer>
    var globalWbCont: WorkbookContainer

    /**
     * @return [WbWsSt] from an existing worksheet, null if such worksheet does not exist
     */
    fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt?
    fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>?
    fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>?
    fun getWsNameSt(wbKeySt:St<WorkbookKey>, wsName: String): St<String>?

    /**
     * @return [WbWsSt] from an existing worksheet, null if such worksheet does not exist
     */
    fun getWbWsSt(wbWs: WbWs): WbWsSt?

    fun getWsRs(wbKey: WorkbookKey, wsName: String): Rs<Worksheet, ErrorReport>
    fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Worksheet, ErrorReport>
    fun getWsRs(wbwsSt: WbWsSt): Rs<Worksheet, ErrorReport>

    fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet?
    fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet?
    fun getWs(wbws: WbWs): Worksheet?
    fun getWs(wbwsSt: WbWsSt): Worksheet?
    fun getWs(wsId:WorksheetIdPrt):Worksheet?

    fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rs<Ms<Worksheet>, ErrorReport>
    fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Ms<Worksheet>, ErrorReport>
    fun getWsMsRs(wbwsSt:WbWsSt): Rs<Ms<Worksheet>, ErrorReport>

    fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>?
    fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>?
    fun getWsMs(wbws: WbWs): Ms<Worksheet>?
    fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>?

    fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>
    fun getRangeRs(rangeId: RangeIdImp): Rs<Range, ErrorReport>
    fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?

    fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?
    fun getLazyRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>
    fun getLazyRangeRs(wbKeySt:St<WorkbookKey> , wsNameSt:St<String> , rangeAddress: RangeAddress): Rs<Range, ErrorReport>

    fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Cell, ErrorReport>
    fun getCellRs(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rs<Cell, ErrorReport>
    fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell?

    /**
     * replace a workbook with a new workbook with the same workbook key
     */
    fun replaceWb(newWb: Workbook): DocumentContainer
}

