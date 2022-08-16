package com.emeraldblast.p6.app.document.range

import com.emeraldblast.p6.app.action.range.RangeId
import com.emeraldblast.p6.app.document.cell.d.Cell
import com.emeraldblast.p6.app.document.range.address.RangeAddress

interface Range {
    val rangeId: RangeId
    val address:RangeAddress
    val cells: List<Cell>
    fun toRangeCopy(): RangeCopy
    val isCell:Boolean
}
