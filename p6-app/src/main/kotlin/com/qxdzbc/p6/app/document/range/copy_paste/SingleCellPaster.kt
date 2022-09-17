package com.qxdzbc.p6.app.document.range.copy_paste

import androidx.compose.runtime.getValue
import com.qxdzbc.common.copiers.binary_copier.BinaryTransferable
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.di.state.app_state.TranslatorContainerMs
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import javax.inject.Inject

/**
 * Paste a range with target being a single cell
 */
class SingleCellPaster @Inject constructor(
    @StateContainerSt private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    @TranslatorContainerMs private val transContMs: Ms<TranslatorContainer>,
) : RangePaster {

    val stateCont by stateContSt

    override fun paste(target: RangeId): Result<Workbook, ErrorReport> {
        try {

            val source: RangeCopy? = this.makeRangeCopyObj(target)
            return this.paste(source, target)
        } catch (e: Throwable) {
            return CommonErrors.ExceptionError.report(e).toErr()
        }
    }

    private fun makeRangeCopyObj(target: RangeId): RangeCopy? {
        val wbwsSt = stateCont.getWbWsSt(target)
        if(wbwsSt!=null){
            val cl:Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val translator = transContMs.value.getTranslatorOrCreate(wbwsSt)
            val bytes:ByteArray = cl.getData(BinaryTransferable.binFlavor) as ByteArray
            val rangeCopy = RangeCopy.fromProtoBytes(bytes, translator)
            return rangeCopy
        } else{
            return null
        }
    }

    private fun paste(source: RangeCopy?, target: RangeId): Result<Workbook, ErrorReport> {
        val rt = stateCont.getWbRs(target.wbKey)
            .flatMap { wb ->
                if(source!=null){
                    wb.getWsRs(target.wsName).map {
                        var tws = it
                        for (cell:Cell in source.cells) {
                            val newCell = cell.shift(
                                source.rangeId.rangeAddress.topLeft,
                                target.rangeAddress.topLeft
                            )
                            tws = tws.addOrOverwrite(newCell)
                        }
                        val newWb = wb.addSheetOrOverwrite(tws)
                        newWb
                    }
                }else{
                    Ok(wb)
                }
            }
        return rt
    }
}
