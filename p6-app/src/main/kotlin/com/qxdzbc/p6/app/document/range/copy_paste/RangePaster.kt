package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
import com.qxdzbc.p6.app.action.range.RangeId

/**
 * paste whatever data in the clipboard into a range
 */
interface RangePaster {
    /**
     * paste whatever data in the clipboard into a range identified by [target]
     * @return a new [Workbook], or an [ErrorReport] if there are errors
     */
    fun paste(target: RangeId):Result<Workbook,ErrorReport>

    fun paste2(target: RangeId):PasteResponse
}
