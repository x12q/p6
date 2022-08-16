package com.emeraldblast.p6.app.document.range.copy_paste

import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
import javax.inject.Inject

class RangePasterImp @Inject constructor(
    val singleCellPaster: SingleCellPaster,
    val rangeRangePasterImp: RangeRangePasterImp,
) : RangePaster {
    override fun paste(targetRangeId: RangeId): Result<Workbook, ErrorReport> {
        if(targetRangeId.rangeAddress.isCell()){
            return singleCellPaster.paste(targetRangeId)
        }else{
            return rangeRangePasterImp.paste(targetRangeId)
        }
    }
}
