package com.qxdzbc.p6.app.document.range

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.state.app_state.DocumentContainerMs
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeIdImp2
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A lazy range is a range that does not hold worksheet reference (hold a St instead), and only fetch the worksheet when it is requested.
 */
data class LazyRange @AssistedInject constructor(
    @Assisted("1") override val address: RangeAddress,
    @Assisted("2") val wsNameSt: St<String>,
    @Assisted("3") val wbKeySt: St<WorkbookKey>,
    @DocumentContainerMs
    val documentContMs: Ms<DocumentContainer>
) : Range{
    val wbKey by wbKeySt
    val worksheet get() = documentContMs.value.getWs(wbKey, wsNameSt.value)
    override val rangeId: RangeId by derivedStateOf {
        RangeIdImp2(
            rangeAddress = this.address,
            wbKeySt = wbKeySt,
            wsNameSt = wsNameSt
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
