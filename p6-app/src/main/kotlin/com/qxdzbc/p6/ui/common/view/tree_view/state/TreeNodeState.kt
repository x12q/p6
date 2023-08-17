package com.qxdzbc.p6.ui.common.view.tree_view.state

import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.common.compose.Ms

interface TreeNodeState {
    val id:String
    val isExpandable: Boolean
    val isExpanded: Boolean
    fun expand():TreeNodeState
    fun collapse():TreeNodeState
    fun switchExpanded():TreeNodeState
    val layoutCoorWrapperMs: Ms<P6LayoutCoor?>
    fun setLayoutCoorWrapper(lcw: P6LayoutCoor?):TreeNodeState
}
