package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.test_util.TestSplitter
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
        state = VerticalEdgeSliderStateImp()
    }

    @Test
    fun setThumbOffsetWhenDrag_1(){
        test("test that thumb offset cannot go belove 0"){
            preCondition {
                state.thumbOffset.y shouldBe 0.dp
            }
            state.setThumbOffsetWhenDrag(mockDensity,-1000f)

            postCondition {
                state.thumbOffset.y shouldBeGreaterThanOrEqualTo 0.dp
            }
        }
    }

    @Test
    fun setThumbOffsetWhenDrag_2(){
        test("test that thumb offset is computed correctly with a reasonable delta"){
            preCondition {
                state.thumbOffset.y shouldBe 0.dp
            }

            state.setThumbOffsetWhenDrag(mockDensity,20f)

            postCondition {
                state.thumbOffset.y shouldBe 20.dp
            }
        }
    }

}