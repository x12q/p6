package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.unit.IntOffset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper.Companion.replaceWith

data class DragAndDropHostStateImp(
    override val isClicked: Boolean = false,
    override val mousePosition: IntOffset? = null,
    override val hostCoorWrapper: LayoutCoorWrapper? = null,
    override val dragMap: Map<Any, LayoutCoorWrapper> = emptyMap(),
    override val dropMap: Map<Any, LayoutCoorWrapper> = emptyMap(),
    override val currentDrag: Any? = null
) : DragAndDropHostState {

    override fun setIsClicked(i: Boolean): DragAndDropHostStateImp {
        return this.copy(isClicked = i)
    }

    override fun setMousePosition(i: IntOffset?): DragAndDropHostStateImp {
        return this.copy(mousePosition = i)
    }

    override fun setCurrentDrag(i: Any?): DragAndDropHostStateImp {
        if (i == currentDrag) {
            return this
        } else {
            return this.copy(currentDrag = i)
        }
    }

    override fun resetToNonDragState(): DragAndDropHostStateImp {
        return this.copy(
            isClicked = false,
            currentDrag = null,
            mousePosition = null
        )
    }

    override fun setDragLayoutCoorWrapper(key: Any, layoutCoorWrapper: LayoutCoorWrapper): DragAndDropHostStateImp {
        return this.copy(dragMap = this.dragMap.updateLayoutMap(key, layoutCoorWrapper))
    }

    override fun removeDragLayoutCoorWrapper(key: Any): DragAndDropHostStateImp {
        return this.copy(
            dragMap = dragMap - key
        )
    }

    override fun setDropLayoutCoorWrapper(key: Any, layoutCoorWrapper: LayoutCoorWrapper): DragAndDropHostStateImp {
        return this.copy(dropMap = this.dropMap.updateLayoutMap(key, layoutCoorWrapper))
    }

    override fun removeDropLayoutCoorWrapper(key: Any): DragAndDropHostStateImp {
        return this.copy(
            dropMap = dropMap - key
        )
    }

    override fun clearDropLayoutCoorWrapper(): DragAndDropHostStateImp {
        return this.copy(dropMap = emptyMap())
    }

    override fun setHostLayoutCoorWrapper(i: LayoutCoorWrapper?): DragAndDropHostStateImp {
        return this.copy(hostCoorWrapper = hostCoorWrapper?.replaceWith(i))
    }

    companion object {
        fun Map<Any, LayoutCoorWrapper>.updateLayoutMap(
            key: Any,
            layoutCoorWrapper: LayoutCoorWrapper
        ): Map<Any, LayoutCoorWrapper> {
            val currentLayout = this[key]
            val replacement = currentLayout.replaceWith(layoutCoorWrapper)
            if (replacement != null) {
                return this + (key to layoutCoorWrapper)
            } else {
                return this - key
            }
//            if (currentLayout != null) {
//                if (currentLayout != layoutCoorWrapper) {
//                    return this + (key to layoutCoorWrapper)
//                } else {
//                    return this + (key to currentLayout.forceRefresh())
//                }
//            } else {
//                return this + (key to layoutCoorWrapper)
//            }
        }
    }
}
