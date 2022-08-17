package com.emeraldblast.p6.ui.app.state

import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.common.Rs
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

    fun getWorkbookRs(wbKey: WorkbookKey): Rs<Workbook, ErrorReport>
    fun getWorkbook(workbookKey: WorkbookKey):Workbook?

    fun getWorksheetRs(wbKey: WorkbookKey, wsName: String): Rs<Worksheet, ErrorReport>
    fun getWorksheet(wbKey: WorkbookKey, wsName: String): Worksheet?
    fun getWorksheet(wbws:WithWbWs): Worksheet?

    fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport>
    fun getRangeRsById(rangeId: RangeId): Rs<Range, ErrorReport>
    fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range?

    fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress):Range?
    fun getLazyRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress):Rs<Range, ErrorReport>

    fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Cell, ErrorReport>
    fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell?

    /**
     * replace a workbook with a new workbook with the same workbook key
     */
    fun replaceWb(newWb: Workbook): DocumentContainer
}

