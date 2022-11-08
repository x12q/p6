package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
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
import com.qxdzbc.p6.app.document.range.LazyRangeFactory
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.squareup.anvil.annotations.ContributesBinding
import java.nio.file.Path
import javax.inject.Inject
@ContributesBinding(P6AnvilScope::class)
class DocumentContainerImp @Inject constructor(
    override val wbContMs: Ms<WorkbookContainer>,
    private val lazyRangeFactory: LazyRangeFactory,
) : DocumentContainer {

    override var wbCont: WorkbookContainer by wbContMs

    override fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt? {
        return this.getWs(wbKey, wsName)?.id
    }

    override fun getWbWsSt(wbWs: WbWs): WbWsSt? {
        return this.getWs(wbWs)?.id
    }

    override fun getWbWsStRs(wbWs: WbWs): Rse<WbWsSt> {
        return this.getWsRs(wbWs).map { it.id }
    }

    override fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>? {
        return this.getWb(wbKey)?.keyMs
    }

    override fun getWbKeyStRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>> {
        return this.getWbRs(wbKey).map { it.keyMs }
    }

    override fun getWbKeyMs(wbKey: WorkbookKey): Ms<WorkbookKey>? {
        return this.getWb(wbKey)?.keyMs
    }

    override fun getWbKeyMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookKey>> {
        return this.getWbRs(wbKey).map { it.keyMs }
    }

    override fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>? {
        return this.getWs(wbKey, wsName)?.nameMs
    }

    override fun getWsNameSt(wbws: WbWs): St<String>? {
        return this.getWs(wbws)?.nameMs
    }

    override fun getWsNameSt(wbKeySt: St<WorkbookKey>, wsName: String): St<String>? {
        return this.getWb(wbKeySt)?.getWs(wsName)?.nameMs
    }

    override fun getWsNameStRs(wbws: WbWs): Rse<St<String>> {
        return this.getWsRs(wbws).map { it.wsNameSt }
    }

    override fun getWsNameMs(wbKey: WorkbookKey, wsName: String): Ms<String>? {
        return this.getWs(wbKey, wsName)?.nameMs
    }

    override fun getWsNameMs(wbKeySt: St<WorkbookKey>, wsName: String): Ms<String>? {
        return this.getWs(wbKeySt.value,wsName)?.nameMs
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        return this.wbCont.getWb(wbKey)
    }

    override fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>? {
        return this.wbCont.getWbMs(wbKeySt)
    }

    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        return this.wbCont.getWbMs(wbKey)
    }

    override fun getWbMsRs(wbKey: WorkbookKey): Result<Ms<Workbook>, ErrorReport> {
        return this.wbCont.getWbMsRs(wbKey)
    }

    override fun getWb(path: Path): Workbook? {
        return this.wbCont.getWb(path)
    }

    override fun getWbRs(path: Path): Result<Workbook, ErrorReport> {
        return this.wbCont.getWbRs(path)
    }

    override fun getWbMsRs(path: Path): Result<Ms<Workbook>, ErrorReport> {
        return this.wbCont.getWbMsRs(path)
    }

    override fun getWb(wbKeySt: St<WorkbookKey>): Workbook? {
        return this.wbCont.getWb(wbKeySt)
    }

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Rs<Ms<Workbook>, ErrorReport> {
        return this.wbCont.getWbMsRs(wbKeySt)
    }

    override fun getWbRs(wbKey: WorkbookKey): Result<Workbook, ErrorReport> {
        return this.wbCont.getWbRs(wbKey)
    }

    override fun getWbRs(wbKeySt: St<WorkbookKey>): Rs<Workbook, ErrorReport> {
        return this.wbCont.getWbRs(wbKeySt)
    }

    override fun getWsRs(wbKey: WorkbookKey, wsName: String): Result<Worksheet, ErrorReport> {
        return this.wbCont.getWbRs(wbKey).flatMap {
            it.getWsRs(wsName)
        }
    }

    override fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Worksheet, ErrorReport> {
        return this.wbCont.getWbRs(wbKeySt).flatMap {
            it.getWsRs(wsNameSt)
        }
    }

    override fun getWsRs(wbwsSt: WbWsSt): Rs<Worksheet, ErrorReport> {
        return this.wbCont.getWbRs(wbwsSt.wbKeySt).flatMap {
            it.getWsRs(wbwsSt.wsNameSt)
        }
    }

    override fun getWsRs(wbws: WbWs): Rs<Worksheet, ErrorReport> {
        return this.wbCont.getWbRs(wbws.wbKey).flatMap {
            it.getWsRs(wbws.wsName)
        }
    }

    override fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet? {
        return this.wbCont.getWb(wbKey)?.getWs(wsName)
    }

    override fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet? {
        return this.wbCont.getWb(wbKeySt)?.getWs(wsNameSt)
    }

    override fun getWs(wbws: WbWs): Worksheet? {
        return this.wbCont.getWb(wbws.wbKey)?.getWs(wbws.wsName)
    }

    override fun getWs(wbwsSt: WbWsSt): Worksheet? {
        return this.wbCont.getWb(wbwsSt.wbKeySt)?.getWs(wbwsSt.wsNameSt)
    }

    override fun getWs(wsId: WorksheetIdWithIndexPrt): Worksheet? {
        val wb: Workbook? = this.wbCont.getWb(wsId.wbKey)
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
        return this.wbCont.getWbRs(wbKey).flatMap { it.getWsMsRs(wsName) }
    }

    override fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Ms<Worksheet>, ErrorReport> {
        return this.wbCont.getWbRs(wbKeySt).flatMap { it.getWsMsRs(wsNameSt) }
    }

    override fun getWsMsRs(wbwsSt: WbWsSt): Rs<Ms<Worksheet>, ErrorReport> {
        return this.wbCont.getWbRs(wbwsSt.wbKeySt).flatMap {
            it.getWsMsRs(wbwsSt.wsNameSt)
        }
    }

    override fun getWsMsRs(wbws: WbWs): Rs<Ms<Worksheet>, ErrorReport> {
        return this.wbCont.getWbRs(wbws.wbKey).flatMap {
            it.getWsMsRs(wbws.wsName)
        }
    }

    override fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>? {
        return this.wbCont.getWb(wbKey)?.getWsMs(wsName)
    }

    override fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>? {
        return this.wbCont.getWb(wbKeySt)?.getWsMs(wsNameSt)
    }

    override fun getWsMs(wbws: WbWs): Ms<Worksheet>? {
        return this.wbCont.getWb(wbws.wbKey)?.getWsMs(wbws.wsName)
    }

    override fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>? {
        return this.wbCont.getWb(wbwsSt.wbKeySt)?.getWsMs(wbwsSt.wsNameSt)
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

    override fun getCellRsOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Result<Cell, ErrorReport> {
        return this.getWsRs(wbKey, wsName).andThen { it.getCellOrDefaultRs(cellAddress) }
    }

    override fun getCellRsOrDefault(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rs<Cell, ErrorReport> {
        return this.getWsRs(wbKeySt, wsNameSt).andThen { it.getCellOrDefaultRs(cellAddress) }
    }

    override fun getCellRsOrDefault(cellId: CellIdDM): Rs<Cell, ErrorReport> {
        return getCellRsOrDefault(cellId.wbKey, cellId.wsName, cellId.address)
    }

    override fun getCellRsOrDefault(cellId: CellId): Rs<Cell, ErrorReport> {
        return getCellRsOrDefault(cellId.wbKeySt,cellId.wsNameSt,cellId.address)
    }

    override fun getCellOrDefault(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell? {
        return this.getCellRsOrDefault(wbKey, wsName, cellAddress).component1()
    }

    override fun getCellOrDefault(wbws: WbWs, cellAddress: CellAddress): Cell? {
        return this.getCellOrDefault(wbws.wbKey, wbws.wsName, cellAddress)
    }

    override fun getCellOrDefault(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>, cellAddress: CellAddress): Cell? {
        val rt= this.getCellMsRs(wbKeySt, wsNameSt, cellAddress).component1()?.value
        return rt
    }

    override fun getCellOrDefault(wbwsSt: WbWsSt, cellAddress: CellAddress): Cell? {
        return this.getCellOrDefault(wbwsSt.wbKeySt,wbwsSt.wsNameSt,cellAddress)
    }

    override fun getCellOrDefault(cellId: CellIdDM): Cell? {
        return getCellRsOrDefault(cellId).component1()
    }

    override fun getCellOrDefault(cellId: CellId): Cell? {
        return this.getCellOrDefault(
            cellId.wbKeySt,cellId.wsNameSt,cellId.address
        )
    }

    override fun getCellMsRs(
        wbKey: WorkbookKey,
        wsName: String,
        cellAddress: CellAddress
    ): Rs<Ms<Cell>, ErrorReport> {
        return getWsRs(wbKey, wsName).flatMap {
            it.getCellMsRs(cellAddress)
        }
    }

    override fun getCellMsRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rs<Ms<Cell>, ErrorReport> {
        return getWsRs(wbKeySt, wsNameSt).flatMap {
            it.getCellMsRs(cellAddress)
        }
    }

    override fun getCellMsRs(cellId: CellIdDM): Rs<Ms<Cell>, ErrorReport> {
        return getCellMsRs(cellId.wbKey, cellId.wsName, cellId.address)
    }

    override fun getCellMs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Ms<Cell>? {
        return getCellMsRs(wbKey, wsName, cellAddress).component1()
    }

    override fun getCellMs(cellId: CellIdDM): Ms<Cell>? {
        return getCellMsRs(cellId).component1()
    }

    override fun replaceWb(newWb: Workbook): DocumentContainer {
        wbCont = wbCont.overwriteWB(newWb)
        return this
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
