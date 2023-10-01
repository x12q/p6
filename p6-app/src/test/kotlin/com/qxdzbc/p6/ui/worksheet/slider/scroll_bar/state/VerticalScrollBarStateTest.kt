package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state

import com.qxdzbc.common.test_util.TestSplitter
import kotlin.test.*

class VerticalScrollBarStateTest : TestSplitter() {

    lateinit var state: VerticalScrollBarState

    @BeforeTest
    fun bt() {
//        state = VerticalEdgeSliderState()
//        state.thumbLayoutCoorMs.value = DummyP6LayoutCoor(
//                boundInWindow = Rect(Offset.Zero,size= Size(width=10f,height=10f)),
//                boundInParent = Rect(Offset.Zero,size= Size(width=10f,height=10f)),
//            )
//
//        state.railLayoutCoorMs.value = DummyP6LayoutCoor(
//            boundInWindow = Rect(Offset.Zero,size= Size(width=10f,height=100f)),
//            boundInParent = null,
//        )
    }

    @Test
    fun projectThumbPositionToIndex(){
//        state.thumbPositionRatioMs.value=0f
//        state.projectThumbPositionToIndex(1 .. 100) shouldBe 1
//
//        state.thumbPositionRatioMs.value=0.1f
//        state.projectThumbPositionToIndex(1 .. 100) shouldBe 20
//
//        state.thumbPositionRatioMs.value=0.25f
//        state.projectThumbPositionToIndex(1 .. 100) shouldBe 50

    }

    @Test
    fun setThumbOffsetWhenDrag_1() {
//        test("test that thumb offset cannot go belove 0") {
//            preCondition {
//                state.thumbPositionRatio shouldBe 0f
//            }
//            state.computeThumbOffsetWhenDrag(-1000f)
//
//            postCondition {
//                state.thumbPositionRatio shouldBeGreaterThanOrEqualTo 0f
//            }
//        }
    }

    @Test
    fun setThumbOffsetWhenDrag_2() {
//        test("test that thumb offset is computed correctly with a reasonable delta") {
//            preCondition {
//                state.thumbPositionRatio shouldBe 0f
//            }
//
//            state.computeThumbOffsetWhenDrag( 20f)
//
//            postCondition {
//                state.thumbPositionRatio shouldBe 20f/state.railLengthPx!!
//            }
//        }
    }

    @Test
    fun recomputeStateWhenThumbReachRailBottom() {
//        test("Correctness") {
//            val oldLengthRatio = state.thumbLengthRatioMs.value
//            state.recomputeStateWhenThumbReachRailBottom()
//            postCondition {
//                state.thumbLengthRatioMs.value shouldBe oldLengthRatio * state.reductionRatio
//                state.thumbPositionRatio shouldBe state.moveBackRatio
//            }
//        }
    }

    @Test
    fun recomputeStateWhenThumbReachRailTop() {
//        test("Correctness") {
//            state.thumbLengthRatioMs.value *= state.reductionRatio
//            preCondition {
//                (state.thumbLengthRatioMs.value < state.maxLengthRatio).shouldBeTrue()
//            }
//
//            state.recomputeStateWhenThumbReachRailTop()
//
//            postCondition {
//                state.thumbLengthRatioMs.value shouldBe state.maxLengthRatio
//            }
//        }
    }

    @Test
    fun recomputeStateWhenThumbIsDragged(){
//        test("drag thumb but thumb does not reach bottom yet"){
//            state.recomputeStateWhenThumbIsDragged(
//                delta = 30f,true
//            )
//            state.thumbPositionRatio shouldBe 30f/state.railLengthPx!!
//
//        }

//        test("drag thumb to the bottom"){
//            val oldThumbLength = state.thumbLengthInPx
//
//            state.recomputeStateWhenThumbIsDragged(
//                delta = 90f,true
//            )
//            state.thumbPositionRatio shouldBe state.moveBackRatio
//            state.thumbLengthInPx shouldBe oldThumbLength*state.reductionRatio
//
//        }
    }

}