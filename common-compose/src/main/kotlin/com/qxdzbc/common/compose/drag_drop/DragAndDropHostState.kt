package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.unit.IntOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper

interface DragAndDropHostState {

    val isClicked:Boolean
    fun setIsClicked(i:Boolean):DragAndDropHostState

    val mousePosition: IntOffset?
    fun setMousePosition(i: IntOffset?): DragAndDropHostState

    val dragObjCoorWrapper:LayoutCoorWrapper?
    fun setDragObjCoorWrapper(i:LayoutCoorWrapper?):DragAndDropHostState

//    val dragReceiverCoorWrappers: List<LayoutCoorWrapper>
    // TODO remember to check for equality, and force refresh.
//    fun setDragReceiverCoorWrappers(i: List<LayoutCoorWrapper>): DragAndDropHostState
    val dropTargetCoorWrapper: LayoutCoorWrapper?
    fun setDropTargetCoorWrapper(i: LayoutCoorWrapper?): DragAndDropHostState

    val dragHostCoorWrapper: LayoutCoorWrapper?
    fun setHostLayoutCoor(i:LayoutCoorWrapper?):DragAndDropHostState
}

