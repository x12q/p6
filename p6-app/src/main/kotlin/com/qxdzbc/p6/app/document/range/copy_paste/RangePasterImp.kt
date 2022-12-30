package com.qxdzbc.p6.app.document.range.copy_paste

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.range.RangeCopy
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = RangePaster::class)
class RangePasterImp @Inject constructor(
    val singleCellPaster: SingleCellPaster,
    val rangeRangePasterImp: RangeRangePasterImp,
    private val stateContainerMs: Ms<StateContainer>,
    override val transContMs: Ms<TranslatorContainer>,
) : BaseRangePaster() {
    override val stateCont: StateContainer by stateContainerMs
    override fun paste(target: RangeId): PasteResponse {
        if(target.rangeAddress.isCell()){
            return singleCellPaster.paste(target)
        }else{
            return rangeRangePasterImp.paste(target)
        }
    }
}
