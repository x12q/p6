package com.qxdzbc.p6.app.document.range

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet

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

    override val cells: List<Cell>
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
