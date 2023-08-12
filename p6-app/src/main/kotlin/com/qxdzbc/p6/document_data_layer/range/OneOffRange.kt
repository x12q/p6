package com.qxdzbc.p6.document_data_layer.range

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.composite_actions.range.RangeIdImp
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet

/**
 * A one-off range hold a hard reference to a worksheet obj. Be extra careful using this one. Prefer [LazyRange].
 */
data class OneOffRange(
    val worksheet: Worksheet,
    val wbKeySt: St<WorkbookKey>,
    override val address: RangeAddress
) : Range {
    val wbKey by wbKeySt
    override val rangeId: RangeId
        get() = RangeIdImp(
            rangeAddress = this.address,
            wbKeySt = wbKeySt,
            wsNameSt = worksheet.nameMs
        )


    override fun equals(other: Any?): Boolean {
        if (other is Range) {
            val c1 = this.rangeId == other.rangeId
            return c1
        } else {
            return false
        }
    }

    override val cells: List<com.qxdzbc.p6.document_data_layer.cell.Cell>
        get() = this.worksheet.cells.filter { this.address.contains(it.address) }

    override fun toRangeCopy(): RangeCopy {
        return RangeCopy(
            rangeId = this.rangeId,
            cells = this.cells
        )
    }

    override fun hashCode(): Int {
        return rangeId.hashCode()
    }

    override val isCell: Boolean get() = address.isCell()

}
