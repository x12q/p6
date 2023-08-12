package com.qxdzbc.p6.document_data_layer.range.copy_paste

import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType = RangePaster::class)
class RangePasterImp @Inject constructor(
    val singleCellPaster: SingleCellPaster,
    val rangeRangePasterImp: RangeRangePasterImp,
    private val stateContainerMs:StateContainer,
    override val transCont: TranslatorContainer,
) : BaseRangePaster() {
    override val stateCont: StateContainer = stateContainerMs
    override fun paste(target: RangeId): PasteResponse {
        if(target.rangeAddress.isCell()){
            return singleCellPaster.paste(target)
        }else{
            return rangeRangePasterImp.paste(target)
        }
    }
}
