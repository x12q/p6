package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper

interface DragAndDropHostState {

    val isClicked:Boolean
    fun setIsClicked(i:Boolean):DragAndDropHostState

    val mousePositionInWindow: Offset?
    fun setMousePositionWindow(i: Offset?): DragAndDropHostState

    val currentDrag:Any?
    fun setCurrentDrag(i:Any?):DragAndDropHostState

    /**
     * Reset this state to the state in which no dragging is happening
     */
    fun resetToNonDragState():DragAndDropHostState

    /**
     * A map of [LayoutCoorWrapper] by some key. This is for identifying the currently dragged item.
     */
    val dragMap:Map<Any,LayoutCoorWrapper>
    fun addDragLayoutCoorWrapper(key:Any, layoutCoorWrapper: LayoutCoorWrapper):DragAndDropHostState
    fun removeDragLayoutCoorWrapper(key:Any):DragAndDropHostState

    val dropMap:Map<Any,LayoutCoorWrapper>
    fun addDropLayoutCoorWrapper(key:Any, layoutCoorWrapper: LayoutCoorWrapper):DragAndDropHostState
    fun removeDropLayoutCoorWrapper(key:Any):DragAndDropHostState
    fun clearDropLayoutCoorWrapper():DragAndDropHostState

    val hostCoorWrapper: LayoutCoorWrapper?
    fun setHostLayoutCoorWrapper(i:LayoutCoorWrapper?):DragAndDropHostState
}

