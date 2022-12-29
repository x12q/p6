package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.workbook.Workbook

data class PasteResponse(
    val sourceRangeId:RangeId?,
    val rs:Rse<Workbook>
)
