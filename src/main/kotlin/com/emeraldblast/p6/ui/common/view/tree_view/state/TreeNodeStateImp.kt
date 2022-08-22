package com.emeraldblast.p6.ui.common.view.tree_view.state

import com.emeraldblast.p6.ui.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.ms
import java.util.*

data class TreeNodeStateImp(
    override val isExpandable: Boolean,
    override val isExpanded: Boolean = false,
    override val layoutCoorWrapperMs: Ms<LayoutCoorWrapper?> = ms(null),
    override val id: String = UUID.randomUUID().toString(),
) : TreeNodeState {
    override fun expand(): TreeNodeState {
        return this.copy(isExpanded=true)
    }

    override fun collapse(): TreeNodeState {
        return this.copy(isExpanded=false)
    }

    override fun switchExpanded(): TreeNodeState {
        return this.copy(isExpanded = !isExpanded)
    }

    override fun setLayoutCoorWrapper(lcw: LayoutCoorWrapper?): TreeNodeState {
        layoutCoorWrapperMs.value = lcw
        return this
    }
}
