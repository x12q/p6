package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.unit.IntOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper

interface DragAndDropHostState {
    val dragObjPosition: IntOffset?
    fun setDragObjPosition(i: IntOffset?): DragAndDropHostState

    val dragReceiverCoorWrappers: List<LayoutCoorWrapper>
    fun setDragReceiverCoorWrappers(i: List<LayoutCoorWrapper>): DragAndDropHostState

    val dragHostCoorWrapper: LayoutCoorWrapper?
}
