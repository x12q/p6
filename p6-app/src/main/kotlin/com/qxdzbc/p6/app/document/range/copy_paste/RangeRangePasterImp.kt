package com.qxdzbc.p6.app.document.range.copy_paste

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import javax.inject.Inject


/**
 * Paste a range with clipboard source being a range. This is used inside [RangePasterImp]
 */
class RangeRangePasterImp @Inject constructor(
    override val stateCont:StateContainer,
    override val transCont: TranslatorContainer
) : BaseRangePaster() {


    companion object {
        fun pasteRs(source: RangeCopy, target: RangeId, wb: Workbook): Rse<Workbook> {
            return wb.getWsRs(target.wsName).map {
                var tws: Worksheet = it
                val sourceRangeAddress: RangeAddress = source.rangeId.rangeAddress
                val targetRangeAddress: RangeAddress = target.rangeAddress
                val targetTopLeft: CellAddress = target.rangeAddress.topLeft
                val sourceTopLeft: CellAddress = sourceRangeAddress.topLeft
                for (col: Int in targetRangeAddress.colRange) {
                    for (row: Int in targetRangeAddress.rowRange) {
                        val targetCellAddress = CellAddress(col, row)
                        // x: reverse shift to get the source address
                        val shiftedSourceAddress: CellAddress = targetCellAddress.shift(targetTopLeft, sourceTopLeft)
                        val sourceCellAddress: CellAddress =
                            sourceRangeAddress.getCellAddressCycle(shiftedSourceAddress)
                        val sourceCell: Cell? = source.cellTable.getElement(sourceCellAddress)
                        if (sourceCell != null) {
                            val newCell = sourceCell
                                .shift(sourceCellAddress, targetCellAddress)
                                .setAddress(targetCellAddress)
                            tws = tws.addOrOverwrite(newCell)
                        }
                    }
                }
                wb.addSheetOrOverwrite(tws)
                wb
            }
        }
    }

    override fun paste(target: RangeId): PasteResponse {
        var sourceRangeId: RangeId? = null
        val rs = try {
            val source: RangeCopy? = this.readDataFromClipboard(target.wbKey, target.wsName)
            sourceRangeId = source?.rangeId
            this.paste(source, target)
        } catch (e: Throwable) {
            CommonErrors.ExceptionError.report(e).toErr()
        }
        val rt = PasteResponse(sourceRangeId, rs)
        return rt
    }

    private fun paste(rangeCopy: RangeCopy?, target: RangeId): Rse<Workbook> {
        val rt: Rse<Workbook> = stateCont.getWbRs(target.wbKey).flatMap { wb ->
            if (rangeCopy != null) {
                pasteRs(rangeCopy, target, wb)
            } else {
                Ok(wb)
            }
        }
        return rt
    }
}
