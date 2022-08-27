package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
import javax.inject.Inject

class RangePasterImp @Inject constructor(
    val singleCellPaster: SingleCellPaster,
    val rangeRangePasterImp: RangeRangePasterImp,
) : RangePaster {
    override fun paste(target: RangeId): Result<Workbook, ErrorReport> {
        if(target.rangeAddress.isCell()){
            return singleCellPaster.paste(target)
        }else{
            return rangeRangePasterImp.paste(target)
        }
    }
}
