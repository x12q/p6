package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.unit.IntOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper

data class DragAndDropHostStateImp(
    override val isClicked: Boolean = false,
    override val mousePosition: IntOffset? = null,
    override val dragObjCoorWrapper: LayoutCoorWrapper? = null,
    override val dropTargetCoorWrapper: LayoutCoorWrapper? = null,
    override val dragHostCoorWrapper: LayoutCoorWrapper? = null
) : DragAndDropHostState{
    override fun setIsClicked(i: Boolean): DragAndDropHostState {
        return this.copy(isClicked = i)
    }

    override fun setMousePosition(i: IntOffset?): DragAndDropHostState {
        return this.copy(mousePosition=i)
    }

    override fun setDragObjCoorWrapper(i: LayoutCoorWrapper?): DragAndDropHostState {
        if(i!=this.dragObjCoorWrapper){
            return this.copy(dragHostCoorWrapper = i)
        }else{
            return this.copy(dragHostCoorWrapper = dragHostCoorWrapper?.forceRefresh())
        }
    }

    override fun setDropTargetCoorWrapper(i: LayoutCoorWrapper?): DragAndDropHostState {
        if(i!=this.dropTargetCoorWrapper){
            return this.copy(dropTargetCoorWrapper = i)
        }else{
            return this.copy(dropTargetCoorWrapper = dropTargetCoorWrapper?.forceRefresh())
        }
    }

    override fun setHostLayoutCoor(i: LayoutCoorWrapper?): DragAndDropHostState {
        if(i!=this.dragHostCoorWrapper){
            return this.copy(dragHostCoorWrapper = i)
        }else{
            return this.copy(dragHostCoorWrapper = dragHostCoorWrapper?.forceRefresh())
        }
    }
}
