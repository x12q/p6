package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.p6.app.common.utils.binary_copier.BinaryCopier
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import javax.inject.Inject

class RangeCopierImp @Inject constructor(
    private val binaryCopier:BinaryCopier
) : RangeCopier {
    override fun copyRange(range: Range): Result<Range, ErrorReport> {
        val data = range.toRangeCopy().toProto().toByteArray()
        val rs = binaryCopier.copyRs(data)
        val rt = rs.map { range }
        return rt
    }
}
