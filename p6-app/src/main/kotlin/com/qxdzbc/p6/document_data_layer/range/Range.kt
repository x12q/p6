package com.qxdzbc.p6.document_data_layer.range

import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress

interface Range {
    val rangeId: RangeId
    val address:RangeAddress
    val cells: List<com.qxdzbc.p6.document_data_layer.cell.Cell>
    fun toRangeCopy(): RangeCopy
    val isCell:Boolean
}
