package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.p6.document_data_layer.range.RangeCopy
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.common.error.CommonErrors
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.document_data_layer.cell.Cell

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


    @Deprecated("don't use, kept just in case")
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
                        for (cell: com.qxdzbc.p6.document_data_layer.cell.Cell in source.cells) {
                            val newCell = cell.shift(
                                source.rangeId.rangeAddress.topLeft,
                                target.rangeAddress.topLeft
                            )
                            tws.addOrOverwrite(newCell)
                        }
                        wb.addSheetOrOverwrite(tws)
                        wb
                    }
                }else{
                    Ok(wb)
                }
            }
        return rt
    }
}
