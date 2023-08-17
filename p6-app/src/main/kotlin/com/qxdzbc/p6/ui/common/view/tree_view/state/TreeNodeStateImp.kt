package com.qxdzbc.p6.ui.common.view.tree_view.state

import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import java.util.*

data class TreeNodeStateImp(
    override val isExpandable: Boolean,
    override val isExpanded: Boolean = false,
    override val layoutCoorWrapperMs: Ms<P6LayoutCoor?> = ms(null),
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

    override fun setLayoutCoorWrapper(lcw: P6LayoutCoor?): TreeNodeState {
        layoutCoorWrapperMs.value = lcw
        return this
    }
}
