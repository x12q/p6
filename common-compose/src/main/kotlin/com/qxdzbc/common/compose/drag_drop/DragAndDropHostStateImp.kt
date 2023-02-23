package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper.Companion.replaceWith

data class DragAndDropHostStateImp(
    override val isDragging: Boolean = false,
    override val mousePositionInWindow: Offset? = null,
    override val hostCoorWrapper: LayoutCoorWrapper? = null,
    override val dragMap: Map<Any, LayoutCoorWrapper> = emptyMap(),
    override val dropMap: Map<Any, LayoutCoorWrapper> = emptyMap(),
    override val currentDrag: Any? = null,
) : DragAndDropHostState {

    override fun setIsDragging(i: Boolean): DragAndDropHostStateImp {
        return this.copy(isDragging = i)
    }

    override fun setMousePositionWindow(i: Offset?): DragAndDropHostState {
        return this.copy(mousePositionInWindow = i)
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
            isDragging = false,
            currentDrag = null,
            mousePositionInWindow = null,
        )
    }

    override fun addDragLayoutCoorWrapper(key: Any, layoutCoorWrapper: LayoutCoorWrapper): DragAndDropHostStateImp {
        return this.copy(dragMap = this.dragMap.updateLayoutMap(key, layoutCoorWrapper))
    }

    override fun removeDragLayoutCoorWrapper(key: Any): DragAndDropHostStateImp {
        return this.copy(
            dragMap = dragMap - key
        )
    }

    override fun addDropLayoutCoorWrapper(key: Any, layoutCoorWrapper: LayoutCoorWrapper): DragAndDropHostStateImp {
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
        return this.copy(hostCoorWrapper = hostCoorWrapper.replaceWith(i))
    }

    /**
     * if 1 drop overlaps the drag-> get that drop
     * if 2+ drops overlap the drag + mouse in one of the drag -> get the one that contain the mouse x or y
     */
    override fun detectDrop(dragId: Any): Any? {
        if (this.isDragging) {
            val state: DragAndDropHostState = this
            val drop = state.dragMap[dragId]?.boundInWindow?.let { dragBoundInWindow ->
                val overlappingDrops = state.dropMap.filter { (dropId, layout) ->
                    layout.boundInWindow?.overlaps(dragBoundInWindow) ?: false
                }.entries
                if (overlappingDrops.isEmpty()) {
                    null
                } else {
                    if (overlappingDrops.size == 1) {
                        // if 1 drop overlaps the drag-> get that drop
                        overlappingDrops.first()
                    } else {
                        /*

                         */
                        // if 2+ drops overlap the drag + mouse in one of the drag -> get the one that contain the mouse, or its x or y
                        val firstDropContainTheMouse = overlappingDrops.firstOrNull { (id, layoutWrapper) ->
                            state.mousePositionInWindow?.let { mousePos ->
                                val containMouse = layoutWrapper.boundInWindow?.contains(mousePos) ?: false
                                containMouse
                            } ?: false
                        }
                        if (firstDropContainTheMouse != null) {
                            firstDropContainTheMouse
                        } else {
                            val fistDropContainMouseXOrY = overlappingDrops.firstOrNull { (id, layoutWrapper) ->
                                state.mousePositionInWindow?.let { mousePos ->
                                    val containMouseX = run {
                                        val minX = layoutWrapper.boundInWindow?.topLeft?.x ?: 0f
                                        val maxX = layoutWrapper.boundInWindow?.bottomRight?.x ?: 0f
                                        mousePos.x in minX..maxX
                                    }
                                    val containMousY = run {
                                        val minY = layoutWrapper.boundInWindow?.topLeft?.y ?: 0f
                                        val maxY = layoutWrapper.boundInWindow?.bottomLeft?.y ?: 0f
                                        mousePos.y in minY..maxY
                                    }

                                    containMouseX || containMousY
                                } ?: false
                            }
                            fistDropContainMouseXOrY
                        }
                    }
                }
            }
            return drop?.key
        } else {
            return null
        }
    }

    companion object {
        /**
         * Update a layout map with a pair of [key] and a [LayoutCoorWrapper]
         */
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
        }
    }


}
