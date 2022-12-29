package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
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

    override fun paste2(target: RangeId): PasteResponse {
        if(target.rangeAddress.isCell()){
            return singleCellPaster.paste2(target)
        }else{
            return rangeRangePasterImp.paste2(target)
        }
    }
}
