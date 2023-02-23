package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.geometry.Offset
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import org.mockito.kotlin.mock
import kotlin.test.*

internal class DragAndDropHostStateImpTest{

    fun makeDraggingState(): DragAndDropHostStateImp {
        val s1 = DragAndDropHostStateImp(
            isClicked = true,
            mousePositionInWindow = Offset.Zero,
            currentDrag = "Something",
        )
        return s1
    }

    @Test
    fun setDragLayoutCoorWrapper_removeDragLayoutCoorWrapper(){
        val key="key"
        val layout:LayoutCoorWrapper = mock()

        val s1 = makeDraggingState()
        s1.dragMap[key].shouldBeNull()

        val s2 = s1.addDragLayoutCoorWrapper(key,layout)
        s2.dragMap[key].shouldNotBeNull()

        val s3 = s2.removeDragLayoutCoorWrapper(key)
        s3.dragMap[key].shouldBeNull()
    }

    @Test
    fun setDropLayoutCoorWrapper_removeDropLayoutCoorWrapper(){
        val key="key"
        val layout:LayoutCoorWrapper = mock()

        val s1 = makeDraggingState()
        s1.dropMap[key].shouldBeNull()

        val s2 = s1.addDropLayoutCoorWrapper(key,layout)
        s2.dropMap[key].shouldNotBeNull()

        val s3 = s2.removeDropLayoutCoorWrapper(key)
        s3.dropMap[key].shouldBeNull()
    }

    @Test
    fun resetToNonDragState(){
        val s1 = makeDraggingState()
        val s2 = s1.resetToNonDragState()
        s2.isClicked.shouldBeFalse()
        s2.mousePositionInWindow.shouldBeNull()
        s2.currentDrag.shouldBeNull()
    }
}
