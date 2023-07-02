package com.qxdzbc.p6.app.document.range

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeIdImp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A lazy range is a range that does not hold worksheet reference (hold a St instead), and only fetch the worksheet when it is requested. This ensures that the range's content is always update to date, thus safer to use.
 * TODO this range hold a reference to document container. It should not be that way.
 */
data class LazyRange @AssistedInject constructor(
    @Assisted("1") override val address: RangeAddress,
    @Assisted("2") val wsNameSt: St<String>,
    @Assisted("3") val wbKeySt: St<WorkbookKey>,
    private val docCont: DocumentContainer,
) : Range{

    val wbKey by wbKeySt

    val worksheet get() = docCont.getWs(wbKey, wsNameSt.value)

    override val rangeId: RangeId get()=RangeIdImp(
        rangeAddress = this.address,
        wbKeySt = wbKeySt,
        wsNameSt = wsNameSt
    )

    override fun equals(other: Any?): Boolean {
        if(other is Range){
            val c1 = this.rangeId == other.rangeId
            return c1
        }else{
            return false
        }
    }


    override val cells: List<Cell>
        get() = this.worksheet?.cells?.filter { this.address.contains(it.address) } ?: emptyList()

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
