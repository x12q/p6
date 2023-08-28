package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.ProjectThumbPosition
import test.BaseAppStateTest
import kotlin.test.*



class EdgeSliderActionImpTest : BaseAppStateTest(){
    lateinit var action: EdgeSliderActionImp
    lateinit var gridSliderMs:Ms<GridSlider>

    object dummyProjecter: ProjectThumbPosition{
        var i:Int = 1
        override fun projectThumbPositionToIndex(indexRange: IntRange): Int {
            return i
        }
    }

    @BeforeTest
    fun bt(){
        gridSliderMs = ms(GridSliderImp.forPreview())
        action = EdgeSliderActionImp(
            sliderMs = gridSliderMs,
            projectThumbPosition =dummyProjecter
        )
    }


    @Test
    fun onDrag(){
        val gs by gridSliderMs
        dummyProjecter.i = 55
        action.onDrag()

        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

        println("=====")
        dummyProjecter.i=200
        action.onDrag()
        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

        println("=====")
        dummyProjecter.i=1
        action.onDrag()
        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

    }

}