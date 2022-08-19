package com.emeraldblast.p6.ui.app.state

import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.Cell
import com.emeraldblast.p6.app.document.range.Range
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.Ms

/**
 * A utility interface providing functions for querying documents' content
 */
interface DocumentContainer {

    val globalWbContMs: Ms<WorkbookContainer>
    var globalWbCont: WorkbookContainer

    /**
     * @return [WbWsSt] from an existing worksheet, null if such worksheet does not exist
     */
    fun getWbWsSt(wbKey: WorkbookKey,wsName: String):WbWsSt?
    /**
     * @return [WbWsSt] from an existing worksheet, null if such worksheet does not exist
     */
    fun getWbWsSt(wbWs: WbWs):WbWsSt?

    fun getWbRs(wbKey: WorkbookKey): Rs<Workbook, ErrorReport>
    fun getWb(wbKey: WorkbookKey):Workbook?

    fun getWsRs(wbKey: WorkbookKey, wsName: String): Rs<Worksheet, ErrorReport>
    fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet?
    fun getWs(wbws:WbWs): Worksheet?

    fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>
    fun getRangeRs(rangeId: RangeId): Rs<Range, ErrorReport>
    fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?

    fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress):Range?
    fun getLazyRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>

    fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Cell, ErrorReport>
    fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell?

    /**
     * replace a workbook with a new workbook with the same workbook key
     */
    fun replaceWb(newWb: Workbook): DocumentContainer
}

