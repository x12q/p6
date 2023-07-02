package com.qxdzbc.p6.app.document.range.copy_paste

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.Cell

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import javax.inject.Inject

/**
 * Paste a range with target being a single cell. This is used inside [RangePasterImp]
 */
class SingleCellPaster @Inject constructor(
    override val stateCont:StateContainer,
    override val transCont: TranslatorContainer,
) : BaseRangePaster() {


    override fun paste(target: RangeId): PasteResponse {
        var sourceRangeId:RangeId?=null
        val rs = try {
            val source: RangeCopy? = this.readDataFromClipboard(target.wbKey,target.wsName)
            sourceRangeId = source?.rangeId
            this.paste(source, target)
        } catch (e: Throwable) {
            CommonErrors.ExceptionError.report(e).toErr()
        }
        val rt = PasteResponse(sourceRangeId,rs)
        return rt
    }

    private fun paste(source: RangeCopy?, target: RangeId): Rse<Workbook> {
        val rt = stateCont.getWbRs(target.wbKey)
            .flatMap { wb ->
                if(source!=null){
                    wb.getWsRs(target.wsName).map {
                        var tws = it
                        for (cell: Cell in source.cells) {
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
