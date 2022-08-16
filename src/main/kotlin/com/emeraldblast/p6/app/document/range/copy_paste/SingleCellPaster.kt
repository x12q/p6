package com.emeraldblast.p6.app.document.range.copy_paste

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.utils.binary_copier.BinaryTransferable
import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.range.RangeCopy
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.*
import java.awt.Toolkit
import javax.inject.Inject

/**
 * Paste a range with target being a single cell
 */
class SingleCellPaster @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>,
) : RangePaster {
    private var appState by appStateMs

    companion object {
        private val cl = Toolkit.getDefaultToolkit().systemClipboard
    }

    override fun paste(targetRangeId: RangeId): Result<Workbook, ErrorReport> {
        try {
            val rangeCopy: RangeCopy = this.makeRangeCopyObj(targetRangeId)
            return this.paste(rangeCopy, targetRangeId)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError.report(e).toErr()
        }
    }

    private fun makeRangeCopyObj(rangeId: RangeId): RangeCopy {
        val translator = appState.getTranslator(rangeId.wbKey, rangeId.wsName)
        val bytes = cl.getData(BinaryTransferable.binFlavor) as ByteArray
        val rangeCopy = RangeCopy.fromProtoBytes(bytes, translator)
        return rangeCopy
    }

    private fun paste(range: RangeCopy, target: RangeId): Result<Workbook, ErrorReport> {
        val rt = appState.getWorkbookRs(target.wbKey)
            .andThen { wb ->
                wb.getWsRs(target.wsName).map {
                    var tws = it
                    for (cell in range.cells) {
                        val newCellAddress =
                            cell.address.shift(
                                range.rangeId.rangeAddress.topLeft,
                                target.rangeAddress.topLeft
                            )
                        val newCell = cell.setAddress(newCellAddress)
                        tws = tws.addOrOverwrite(newCell)
                    }
                    val newWb = wb.addSheetOrOverwrite(tws)
                    newWb
                }
            }
        return rt
    }
}
