package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.LazyRangeFactory
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.app_state.WbContainerMs
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt
import java.nio.file.Path
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

    override fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>? {
        return this.getWb(wbKey)?.keyMs
    }

    override fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>? {
        return this.getWs(wbKey, wsName)?.nameMs
    }

    override fun getWsNameSt(wbKeySt: St<WorkbookKey>, wsName: String): St<String>? {
        return this.getWb(wbKeySt)?.getWs(wsName)?.nameMs
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        return this.globalWbCont.getWb(wbKey)
    }

    override fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>? {
        return this.globalWbCont.getWbMs(wbKeySt)
    }

    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        return this.globalWbCont.getWbMs(wbKey)
    }

    override fun getWbMsRs(wbKey: WorkbookKey): Result<Ms<Workbook>, ErrorReport> {
        return this.globalWbCont.getWbMsRs(wbKey)
    }

    override fun getWb(path: Path): Workbook? {
        return this.globalWbCont.getWb(path)
    }

    override fun getWbRs(path: Path): Result<Workbook, ErrorReport> {
        return this.globalWbCont.getWbRs(path)
    }

    override fun getWbMsRs(path: Path): Result<Ms<Workbook>, ErrorReport> {
        return this.globalWbCont.getWbMsRs(path)
    }

    override fun getWb(wbKeySt: St<WorkbookKey>): Workbook? {
        return this.globalWbCont.getWb(wbKeySt)
    }

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Rs<Ms<Workbook>, ErrorReport> {
        return this.globalWbCont.getWbMsRs(wbKeySt)
    }

    override fun getWbRs(wbKey: WorkbookKey): Result<Workbook, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKey)
    }

    override fun getWbRs(wbKeySt: St<WorkbookKey>): Rs<Workbook, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKeySt)
    }

    override fun getWsRs(wbKey: WorkbookKey, wsName: String): Result<Worksheet, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKey).flatMap {
            it.getWsRs(wsName)
        }
    }

    override fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Worksheet, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKeySt).flatMap {
            it.getWsRs(wsNameSt)
        }
    }

    override fun getWsRs(wbwsSt: WbWsSt): Rs<Worksheet, ErrorReport> {
        return this.globalWbCont.getWbRs(wbwsSt.wbKeySt).flatMap {
            it.getWsRs(wbwsSt.wsNameSt)
        }
    }

    override fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet? {
        return this.globalWbCont.getWb(wbKey)?.getWs(wsName)
    }

    override fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet? {
        return this.globalWbCont.getWb(wbKeySt)?.getWs(wsNameSt)
    }

    override fun getWs(wbws: WbWs): Worksheet? {
        return this.globalWbCont.getWb(wbws.wbKey)?.getWs(wbws.wsName)
    }

    override fun getWs(wbwsSt: WbWsSt): Worksheet? {
        return this.globalWbCont.getWb(wbwsSt.wbKeySt)?.getWs(wbwsSt.wsNameSt)
    }

    override fun getWs(wsId: WorksheetIdPrt): Worksheet? {
        val wb: Workbook? = this.globalWbCont.getWb(wsId.wbKey)
        if (wb != null) {
            if (wsId.wsName != null) {
                return wb.getWs(wsId.wsName)
            }
            if (wsId.wsIndex != null) {
                return wb.getWs(wsId.wsIndex)
            }
            return null
        } else {
            return null
        }
    }

    override fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rs<Ms<Worksheet>, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKey).flatMap { it.getWsMsRs(wsName) }
    }

    override fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Ms<Worksheet>, ErrorReport> {
        return this.globalWbCont.getWbRs(wbKeySt).flatMap { it.getWsMsRs(wsNameSt) }
    }

    override fun getWsMsRs(wbwsSt: WbWsSt): Rs<Ms<Worksheet>, ErrorReport> {
        return this.globalWbCont.getWbRs(wbwsSt.wbKeySt).flatMap {
            it.getWsMsRs(wbwsSt.wsNameSt)
        }
    }

    override fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>? {
        return this.globalWbCont.getWb(wbKey)?.getWsMs(wsName)
    }

    override fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>? {
        return this.globalWbCont.getWb(wbKeySt)?.getWsMs(wsNameSt)
    }

    override fun getWsMs(wbws: WbWs): Ms<Worksheet>? {
        return this.globalWbCont.getWb(wbws.wbKey)?.getWsMs(wbws.wsName)
    }

    override fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>? {
        return this.globalWbCont.getWb(wbwsSt.wbKeySt)?.getWsMs(wbwsSt.wsNameSt)
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

    override fun getLazyRangeRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        rangeAddress: RangeAddress
    ): Rs<Range, ErrorReport> {
        val rt = this.getWbRs(wbKeySt).flatMap { wb ->
            wb.getWsRs(wsNameSt).map { ws ->
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

    override fun getCellRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rs<Cell, ErrorReport> {
        return this.getWsRs(wbKeySt, wsNameSt).andThen { it.getCellOrDefaultRs(cellAddress) }
    }

    override fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell? {
        return this.getCellRs(wbKey, wsName, cellAddress).component1()
    }

    override fun replaceWb(newWb: Workbook): DocumentContainer {
        globalWbCont = globalWbCont.overwriteWB(newWb)
        return this
    }

    override fun getRangeRs(rangeId: RangeIdImp): Result<Range, ErrorReport> {
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
