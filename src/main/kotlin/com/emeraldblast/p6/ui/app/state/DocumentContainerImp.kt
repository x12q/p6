package com.emeraldblast.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.common_data_structure.WbWsSt
import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.cell.d.Cell
import com.emeraldblast.p6.app.document.range.LazyRangeFactory
import com.emeraldblast.p6.app.document.range.Range
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.di.state.app_state.WbContainerMs
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import javax.inject.Inject

class DocumentContainerImp @Inject constructor(
    @WbContainerMs override val globalWbContMs: Ms<WorkbookContainer>,
    private val lazyRangeFactory: LazyRangeFactory,
) : DocumentContainer {

    override var globalWbCont: WorkbookContainer by globalWbContMs
    override fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt? {
        return this.getWs(wbKey, wsName)?.id
    }

    override fun getWbWsSt(wbWs: WbWs): WbWsSt? {
        return this.getWs(wbWs)?.id
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        return this.globalWbCont.getWb(wbKey)
    }

    override fun getWsRs(wbKey: WorkbookKey, wsName: String): Result<Worksheet, ErrorReport> {
        return this.getWbRs(wbKey).andThen { wb -> wb.getWsRs(wsName) }
    }

    override fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet? {
        return getWsRs(wbKey, wsName).component1()
    }

    override fun getWs(wbws: WbWs): Worksheet? {
        return getWs(wbws.wbKey, wbws.wsName)
    }

    override fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return this.getRangeRs(wbKey, wsName, rangeAddress).component1()
    }

    override fun getLazyRange(
        wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress
    ): Range? {
        return getLazyRangeRs(wbKey, wsName, rangeAddress).component1()
    }

    override fun getLazyRangeRs(
        wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress
    ): Rs<Range, ErrorReport> {
        val rt = this.getWbRs(wbKey).flatMap { wb ->
            wb.getWsRs(wsName).map { ws ->
                lazyRangeFactory.create(
                    rangeAddress, ws.nameMs, wb.keyMs
                )
            }
        }
        return rt
    }


    override fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Result<Cell, ErrorReport> {
        return this.getWsRs(wbKey, wsName).andThen { it.getCellOrDefaultRs(cellAddress) }
    }

    override fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell? {
        return this.getCellRs(wbKey, wsName, cellAddress).component1()
    }

    override fun replaceWb(newWb: Workbook): DocumentContainer {
        globalWbCont = globalWbCont.overwriteWB(newWb)
        return this
    }

    override fun getWbRs(wbKey: WorkbookKey): Result<Workbook, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKey)
    }

    override fun getRangeRs(rangeId: RangeId): Result<Range, ErrorReport> {
        val rt = this.getWbRs(rangeId.wbKey).andThen { wb ->
            wb.getWsRs(rangeId.wsName).andThen { ws ->
                ws.range(rangeId.rangeAddress)
            }
        }
        return rt
    }

    override fun getRangeRs(
        wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress
    ): Result<Range, ErrorReport> {
        val rt = this.getWsRs(wbKey, wsName).andThen { ws -> ws.range(rangeAddress) }
        return rt
    }

}
