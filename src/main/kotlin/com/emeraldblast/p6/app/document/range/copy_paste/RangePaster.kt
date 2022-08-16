package com.emeraldblast.p6.app.document.range.copy_paste

import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

/**
 * paste whatever data in the clipboard into a range
 */
interface RangePaster {
    /**
     * paste whatever data in the clipboard into a range identified by [targetRangeId]
     * @return a new [Workbook], or an [ErrorReport] if there are errors
     */
    fun paste(targetRangeId: RangeId):Result<Workbook,ErrorReport>
}
