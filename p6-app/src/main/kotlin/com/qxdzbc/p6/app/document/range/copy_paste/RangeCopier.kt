package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.common.Rse

interface RangeCopier {
    fun copyRange(range: Range):Rse<Range>
}
