package com.qxdzbc.p6.app.document.range

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.common.compose.St

data class RangeImp(
    val worksheet: Worksheet,
    val wbKeySt: St<WorkbookKey>,
    override val address: RangeAddress
) : Range {
    val wbKey by wbKeySt
    override val rangeId: RangeId by derivedStateOf {
        RangeId(
            rangeAddress = this.address,
            wbKey = wbKeySt.value,
            wsName = worksheet.name
        )
    }

    override fun equals(other: Any?): Boolean {
        if(other is Range){
            val c1 = this.rangeId == other.rangeId
            return c1
        }else{
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
