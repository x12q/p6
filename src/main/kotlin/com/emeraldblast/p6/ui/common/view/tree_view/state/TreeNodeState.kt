package com.emeraldblast.p6.ui.common.view.tree_view.state

import com.emeraldblast.p6.ui.common.compose.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.Ms

interface TreeNodeState {
    val id:String
    val isExpandable: Boolean
    val isExpanded: Boolean
    fun expand():TreeNodeState
    fun collapse():TreeNodeState
    fun switchExpanded():TreeNodeState
    val layoutCoorWrapperMs: Ms<LayoutCoorWrapper?>
    fun setLayoutCoorWrapper(lcw:LayoutCoorWrapper?):TreeNodeState
}
