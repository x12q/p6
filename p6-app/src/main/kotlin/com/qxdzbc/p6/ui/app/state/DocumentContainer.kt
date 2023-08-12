package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.range.Range
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.wb_container.WorkbookContainer
import com.qxdzbc.p6.document_data_layer.wb_container.WorkbookGetter
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
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

    fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<Worksheet>>
    fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Ms<Worksheet>>
    fun getWsMsRs(wbwsSt:WbWsSt): Rse<Ms<Worksheet>>
    fun getWsMsRs(wbws:WbWs): Rse<Ms<Worksheet>>

    fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>?
    fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>?
    fun getWsMs(wbws: WbWs): Ms<Worksheet>?
    fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>?

    fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rse<Range>
    fun getRangeRs(rangeId: RangeId): Rse<Range>
    fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?

    fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?
    fun getLazyRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rse<Range>
    fun getLazyRangeRs(wbKeySt:St<WorkbookKey> , wsNameSt:St<String> , rangeAddress: RangeAddress): Rse<Range>

    fun getCellRsOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>
    fun getCellRsOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>
    fun getCellRsOrDefault(cellId: CellIdDM): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>
    fun getCellRsOrDefault(cellId: CellId): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>

    fun getCellOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): com.qxdzbc.p6.document_data_layer.cell.Cell?
    fun getCellOrDefault(wbws: WbWs, cellAddress: CellAddress): com.qxdzbc.p6.document_data_layer.cell.Cell?
    fun getCellOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): com.qxdzbc.p6.document_data_layer.cell.Cell?
    fun getCellOrDefault(wbwsSt: WbWsSt, cellAddress: CellAddress): com.qxdzbc.p6.document_data_layer.cell.Cell?
    fun getCellOrDefault(cellId: CellIdDM): com.qxdzbc.p6.document_data_layer.cell.Cell?
    fun getCellOrDefault(cellId: CellId): com.qxdzbc.p6.document_data_layer.cell.Cell?

    fun getCellMsRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rse<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>
    fun getCellMsRs(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rse<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>
    fun getCellMsRs(cellId: CellIdDM): Rse<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    fun getCellMs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>?
    fun getCellMs(cellIdDM: CellIdDM): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>?
    fun getCellMs(cellId: CellId): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>?

    fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>
    fun getCellRs(wbKeySt: St<WorkbookKey>, wsNameSt:St<String>, cellAddress: CellAddress): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>
    fun getCellRs(cellId: CellIdDM): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>

    fun getCell(cellIdDM:CellIdDM): com.qxdzbc.p6.document_data_layer.cell.Cell?
    fun getCell(cellId:CellId): com.qxdzbc.p6.document_data_layer.cell.Cell?

    fun getCellIdRs(cellIdDM:CellIdDM):Rse<CellId>
    fun getCellId(cellIdDM:CellIdDM):CellId?

    /**
     * replace a workbook with a new workbook with the same workbook key
     */
    fun replaceWb(newWb: Workbook)
}

