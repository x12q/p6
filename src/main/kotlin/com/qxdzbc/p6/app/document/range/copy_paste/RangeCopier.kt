package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

interface RangeCopier {
    fun copyRange(range: Range):Result<Range,ErrorReport>
}
