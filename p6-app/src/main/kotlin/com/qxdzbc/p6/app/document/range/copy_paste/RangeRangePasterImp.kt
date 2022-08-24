package com.qxdzbc.p6.app.document.range.copy_paste

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.common.copiers.binary_copier.BinaryTransferable
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import java.awt.Toolkit
import javax.inject.Inject


/**
 * Paste a range with clipboard source being a range
 */
class RangeRangePasterImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>,
) : RangePaster {
    private var appState by appStateMs

    companion object {
        private val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        fun pasteRs(rangeCopy: RangeCopy, target: RangeId, wb: Workbook): Result<Workbook, ErrorReport> {
            return wb.getWsRs(target.wsName).map {
                var tws = it
                val sourceRangeAddress: RangeAddress = rangeCopy.rangeId.rangeAddress
                val targetRangeAddress: RangeAddress = target.rangeAddress
                val targetTopLeft: CellAddress = target.rangeAddress.topLeft
                val sourceTopLeft: CellAddress = sourceRangeAddress.topLeft
                for (col: Int in targetRangeAddress.colRange) {
                    for (row: Int in targetRangeAddress.rowRange) {
                        val targetCellAddress = CellAddress(col, row)
                        val shiftedTarget: CellAddress = targetCellAddress.shift(targetTopLeft, sourceTopLeft)
                        val sourceCellAddress: CellAddress = sourceRangeAddress.getCellAddressCycle(shiftedTarget)
                        val sourceCell: Cell? = rangeCopy.cellTable.getElement(sourceCellAddress)
                        if (sourceCell != null) {
                            val newCell = sourceCell.setAddress(targetCellAddress)
                            tws = tws.addOrOverwrite(newCell)
                        }
                    }
                }
                val newWb = wb.addSheetOrOverwrite(tws)
                newWb
            }
        }
    }

    override fun paste(targetRangeId: RangeId): Result<Workbook, ErrorReport> {
        try {
            val rangeCopy: RangeCopy? = getRangeCopyFromClipboard(targetRangeId.wbKey, targetRangeId.wsName)
            return this.paste(rangeCopy, targetRangeId)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError.report(e).toErr()
        }
    }

    /**
     * extract the range copy from the clipboard
     */
    private fun getRangeCopyFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy? {
        val wbwsSt: WbWsSt? = appState.getWbWsSt(wbKey, wsName)
        if(wbwsSt!=null){
            val translator = appState.translatorContainer.getTranslatorOrCreate(wbwsSt)
            val bytes = clipboard.getData(BinaryTransferable.binFlavor) as ByteArray
            val rangeCopy = RangeCopy.fromProtoBytes(bytes, translator)
            return rangeCopy
        }else{
            return null
        }
    }

    fun paste(rangeCopy: RangeCopy?, target: RangeId): Result<Workbook, ErrorReport> {
        val rt = appState.getWbRs(target.wbKey).flatMap { wb ->
            if(rangeCopy!=null){
                pasteRs(rangeCopy, target, wb)
            }else{
                Ok(wb)
            }
        }
        return rt
    }
}
