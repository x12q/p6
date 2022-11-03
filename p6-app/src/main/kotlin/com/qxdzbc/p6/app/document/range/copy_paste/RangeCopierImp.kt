package com.qxdzbc.p6.app.document.range.copy_paste

import com.qxdzbc.common.copiers.binary_copier.BinaryCopier
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeCopierImp @Inject constructor(
    private val binaryCopier: BinaryCopier
) : RangeCopier {
    override fun copyRange(range: Range): Result<Range, ErrorReport> {
        val data = range.toRangeCopy().toProto().toByteArray()
        val rs = binaryCopier.copyRs(data)
        val rt = rs.map { range }
        return rt
    }
}
