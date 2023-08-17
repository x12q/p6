package com.qxdzbc.common.compose.drag_drop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.qxdzbc.common.compose.layout_coor_wrapper.DummyP6LayoutCoor
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.common.test_util.TestSplitter
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.mockito.kotlin.mock
import kotlin.test.Test

internal class DragAndDropHostStateImpTest : TestSplitter() {

    fun makeDraggingState(): DragAndDropHostInternalStateImp {
        val s1 = DragAndDropHostInternalStateImp(
            isDragging = true,
            mousePositionInWindow = Offset.Zero,
            currentDrag = "Something",
        )
        return s1
    }
    @Test
    fun detectDrop_3() {
        test("drag r1 overlap drop d1 and d2, mouse outside of both, but also casts shadow over d1") {
            val mousePos: Offset = Offset(200f,18f)
            val r1: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(5f, 22f), Offset(20f, 33f))
            )
            val d1: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(0f, 15f), Offset(25f, 25f))
            )
            val d2: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(0f, 30f), Offset(40f, 40f))
            )
            preCondition {
                r1.boundInWindow!!.overlaps(d1.boundInWindow!!).shouldBeTrue()
                r1.boundInWindow!!.overlaps(d2.boundInWindow!!).shouldBeTrue()
                d1.boundInWindow!!.contains(mousePos).shouldBeFalse()
                d2.boundInWindow!!.contains(mousePos).shouldBeFalse()
            }
            val s1 = makeDraggingState()
                .copy(
                    dropMap = mapOf(
                        "d1" to d1,
                        "d2" to d2,
                    ),
                    dragMap = mapOf(
                        "r1" to r1,
                        "r2" to mock(),
                    ),
                    mousePositionInWindow = mousePos
                )
            postCondition {
                s1.detectDrop("r1") shouldBe "d1"
            }
        }
    }

    @Test
    fun detectDrop_2() {
        test("drag r1 overlap drop d1 and d2, mouse in d2, but also casts shadow over d1 (mouse x or y is in d1's ranges)") {
            val mousePos: Offset = Offset(13f, 36f)
            val r1: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(5f, 22f), Offset(20f, 33f))
            )
            val d1: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(0f, 15f), Offset(25f, 25f))
            )
            val d2: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(0f, 30f), Offset(40f, 40f))
            )
            preCondition {
                r1.boundInWindow!!.overlaps(d1.boundInWindow!!).shouldBeTrue()
                r1.boundInWindow!!.overlaps(d2.boundInWindow!!).shouldBeTrue()
                d1.boundInWindow!!.contains(mousePos).shouldBeFalse()
                d2.boundInWindow!!.contains(mousePos).shouldBeTrue()
            }
            val s1 = makeDraggingState()
                .copy(
                    dropMap = mapOf(
                        "d1" to d1,
                        "d2" to d2,
                    ),
                    dragMap = mapOf(
                        "r1" to r1,
                        "r2" to mock(),
                    ),
                    mousePositionInWindow = mousePos
                )
            postCondition {
                s1.detectDrop("r1") shouldBe "d2"
            }
        }
    }

    @Test
    fun detectDrop_1() {
        test("drag r1 overlap drop d1") {
            val mousePos: Offset = mock()
            val r1: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(10f, 10f), Offset(20f, 20f))
            )
            val d1: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(0f, 15f), Offset(25f, 25f))
            )
            val d2: P6LayoutCoor = DummyP6LayoutCoor(
                boundInWindow = Rect(Offset(0f, 30f), Offset(40f, 40f))
            )

            preCondition {
                r1.boundInWindow!!.overlaps(d1.boundInWindow!!).shouldBeTrue()
                r1.boundInWindow!!.overlaps(d2.boundInWindow!!).shouldBeFalse()
            }
            val s1 = makeDraggingState()
                .copy(
                    dropMap = mapOf(
                        "d1" to d1,
                        "d2" to d2,
                    ),
                    dragMap = mapOf(
                        "r1" to r1,
                        "r2" to mock(),
                    ),
                    mousePositionInWindow = mousePos
                )
            postCondition {
                s1.detectDrop("r1") shouldBe "d1"
            }
        }
    }

    @Test
    fun setDragLayoutCoorWrapper_removeDragLayoutCoorWrapper() {
        val key = "key"
        val layout: P6LayoutCoor = mock()

        val s1 = makeDraggingState()
            .setAcceptableDragIds(setOf(key))
        s1.dragMap[key].shouldBeNull()

        val s2 = s1.addDragLayoutCoorWrapper(key, layout)
        s2.dragMap[key].shouldNotBeNull()

        val s3 = s2.removeDragLayoutCoorWrapper(key)
        s3.dragMap[key].shouldBeNull()
    }

    @Test
    fun setDropLayoutCoorWrapper_removeDropLayoutCoorWrapper() {
        val key = "key"
        val layout: P6LayoutCoor = mock()

        val s1 = makeDraggingState()
            .setAcceptableDropIds(setOf(key))
        s1.dropMap[key].shouldBeNull()

        val s2 = s1.addDropLayoutCoorWrapper(key, layout)
        s2.dropMap[key].shouldNotBeNull()

        val s3 = s2.removeDropLayoutCoorWrapper(key)
        s3.dropMap[key].shouldBeNull()
    }

    @Test
    fun resetToNonDragState() {
        val s1 = makeDraggingState()
        val s2 = s1.resetToNonDragState()
        s2.isDragging.shouldBeFalse()
        s2.mousePositionInWindow.shouldBeNull()
        s2.currentDrag.shouldBeNull()
    }
}
