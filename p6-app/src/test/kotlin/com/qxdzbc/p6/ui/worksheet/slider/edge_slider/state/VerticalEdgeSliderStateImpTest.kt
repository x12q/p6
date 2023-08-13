package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import kotlin.test.*

class VerticalEdgeSliderStateImpTest:TestSplitter(){

    lateinit var state: VerticalEdgeSliderStateImp
    val mockDensity = object :Density{
        override val density: Float
            get() = 1f
        override val fontScale: Float
            get() = 1f
    }

    @BeforeTest
    fun bt(){
        state = VerticalEdgeSliderStateImp(
            sliderStateMs = ms(GridSliderImp.forPreview())
        )
    }

    @Test
    fun setThumbOffsetWhenDrag_1(){
        test("test that thumb offset cannot go belove 0"){
            preCondition {
                state.thumbPosition.y shouldBe 0.dp
            }
            state.setThumbOffsetWhenDrag(mockDensity, -1000f,100.dp)

            postCondition {
                state.thumbPosition.y shouldBeGreaterThanOrEqualTo 0.dp
            }
        }
    }

    @Test
    fun setThumbOffsetWhenDrag_2(){
        test("test that thumb offset is computed correctly with a reasonable delta"){
            preCondition {
                state.thumbPosition.y shouldBe 0.dp
            }

            state.setThumbOffsetWhenDrag(mockDensity, 20f,100.dp)

            postCondition {
                state.thumbPosition.y shouldBe 20.dp
            }
        }
    }

    @Test
    fun recomputeStateWhenThumbReachRailBottom(){
        test("Correctness"){
            val oldLengthRatio = state.thumbLengthRatioMs.value
            state.recomputeStateWhenThumbReachRailBottom(100.dp)
            postCondition {
                state.thumbLengthRatioMs.value shouldBe oldLengthRatio*state.reductionRatio
            }
        }
    }

    @Test
    fun recomputeStateWhenThumbReachRailTop(){
        test("Correctness"){
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