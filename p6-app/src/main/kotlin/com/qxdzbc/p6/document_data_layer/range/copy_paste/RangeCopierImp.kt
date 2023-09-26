package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.common.copiers.binary_copier.BinaryCopier
import com.qxdzbc.p6.document_data_layer.range.Range
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeCopierImp @Inject constructor(
    private val binaryCopier: BinaryCopier
) : RangeCopier {
    override fun copyRange(range: Range): Rse<Range> {
        val data = range.toRangeCopy().toProto().toByteArray()
        val rs = binaryCopier.copyRs(data)
        val rt = rs.map { range }
        return rt
    }
}
