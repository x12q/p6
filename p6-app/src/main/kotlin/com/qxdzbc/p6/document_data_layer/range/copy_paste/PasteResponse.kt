package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.document_data_layer.workbook.Workbook

data class PasteResponse(
    val sourceRangeId:RangeId?,
    val rs:Rse<Workbook>
)
