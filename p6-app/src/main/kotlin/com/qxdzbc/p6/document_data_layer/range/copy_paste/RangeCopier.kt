package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.p6.document_data_layer.range.Range
import com.qxdzbc.common.Rse

interface RangeCopier {
    fun copyRange(range: Range):Rse<Range>
}
