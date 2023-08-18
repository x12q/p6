package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.common.test_util.TestSplitter
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import test.MockObjects
import kotlin.test.*

class VerticalEdgeSliderStateImpTest : TestSplitter() {

    lateinit var state: VerticalEdgeSliderStateImp

    val mockDensity = MockObjects.mockDensity

    @BeforeTest
    fun bt() {
        state = VerticalEdgeSliderStateImp()

        state.thumbLayoutCoorMs.value = mock<P6LayoutCoor>{
            whenever(it.boundInWindow) doReturn Rect(Offset.Zero,size= Size(10f,10f))
        }

        state.railLayoutCoorMs.value = mock<P6LayoutCoor>{
            whenever(it.boundInWindow) doReturn Rect(Offset.Zero,size= Size(10f,100f))
        }

    }

    @Test
    fun setThumbOffsetWhenDrag_1() {
        test("test that thumb offset cannot go belove 0") {
            preCondition {
                state.thumbPositionRatio shouldBe 0f
            }
            state.computeThumbOffsetWhenDrag(-1000f)

            postCondition {
                state.thumbPositionRatio shouldBeGreaterThanOrEqualTo 0f
            }
        }
    }

    @Test
    fun setThumbOffsetWhenDrag_2() {
        test("test that thumb offset is computed correctly with a reasonable delta") {
            preCondition {
                state.thumbPositionRatio shouldBe 0f
            }

            state.computeThumbOffsetWhenDrag( 20f)

            postCondition {
                state.thumbPositionRatio shouldBe 0.2f
            }
        }
    }

    @Test
    fun recomputeStateWhenThumbReachRailBottom() {
        test("Correctness") {
            val oldLengthRatio = state.thumbLengthRatioMs.value
            state.recomputeStateWhenThumbReachRailBottom()
            postCondition {
                state.thumbLengthRatioMs.value shouldBe oldLengthRatio * state.reductionRatio
            }
        }
    }

    @Test
    fun recomputeStateWhenThumbReachRailTop() {
        test("Correctness") {
            state.thumbLengthRatioMs.value *= state.reductionRatio
            preCondition {
                (state.thumbLengthRatioMs.value < state.maxLengthRatio).shouldBeTrue()
            }

            state.recomputeStateWhenThumbReachRailTop()

            postCondition {
                state.thumbLengthRatioMs.value shouldBe state.maxLengthRatio
            }
        }
    }

}