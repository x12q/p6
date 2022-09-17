package com.qxdzbc.p6.app.document.range.copy_paste

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.common.copiers.binary_copier.BinaryTransferable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.di.state.app_state.TranslatorContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import java.awt.Toolkit
import javax.inject.Inject


/**
 * Paste a range with clipboard source being a range
 */
class RangeRangePasterImp @Inject constructor(
    @StateContainerSt private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    @TranslatorContainerMs private val transContMs:Ms<TranslatorContainer>
) : RangePaster {
    val stateCont by stateContSt

    companion object {
        fun pasteRs(source: RangeCopy, target: RangeId, wb: Workbook): Result<Workbook, ErrorReport> {
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
                        val sourceCellAddress: CellAddress = sourceRangeAddress.getCellAddressCycle(shiftedSourceAddress)
                        val sourceCell: Cell? = source.cellTable.getElement(sourceCellAddress)
                        if (sourceCell != null) {
                            val newCell = sourceCell
                                .shift(sourceCellAddress,targetCellAddress)
                                .setAddress(targetCellAddress)
                            tws = tws.addOrOverwrite(newCell)
                        }
                    }
                }
                val newWb = wb.addSheetOrOverwrite(tws)
                newWb
            }
        }
    }

    override fun paste(target: RangeId): Result<Workbook, ErrorReport> {
        try {
            val rangeCopy: RangeCopy? = getRangeCopyFromClipboard(target.wbKey, target.wsName)
            return this.paste(rangeCopy, target)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError.report(e).toErr()
        }
    }

    /**
     * extract the range copy from the clipboard
     */
    private fun getRangeCopyFromClipboard(wbKey: WorkbookKey, wsName: String): RangeCopy? {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val wbwsSt: WbWsSt? = stateCont.getWbWsSt(wbKey, wsName)
        if(wbwsSt!=null){
            val translator = transContMs.value.getTranslatorOrCreate(wbwsSt)
            val bytes = clipboard.getData(BinaryTransferable.binFlavor) as ByteArray
            val rangeCopy = RangeCopy.fromProtoBytes(bytes, translator)
            return rangeCopy
        }else{
            return null
        }
    }

    private fun paste(rangeCopy: RangeCopy?, target: RangeId): Result<Workbook, ErrorReport> {
        val rt:Result<Workbook,ErrorReport> = stateContSt.value.getWbRs(target.wbKey).flatMap { wb ->
            if(rangeCopy!=null){
                pasteRs(rangeCopy, target, wb)
            }else{
                Ok(wb)
            }
        }
        return rt
    }
}
