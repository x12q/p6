package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.InternalEdgeSliderActionImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import test.BaseAppStateTest
import kotlin.test.*



class EdgeSliderActionImpTest : BaseAppStateTest(){
    lateinit var action: InternalEdgeSliderActionImp
    lateinit var gridSliderMs:Ms<GridSlider>

    object dummyProjecter: ThumbPositionConverter{
        var i:Int = 1
        override fun convertThumbPositionToIndex(indexRange: IntRange): Int {
            return i
        }
    }

    @BeforeTest
    fun bt(){
        gridSliderMs = ms(GridSliderImp.forPreview())
        action = InternalEdgeSliderActionImp(
            wsStateGetter = WorksheetStateGetter { TODO() },
            sliderMs = gridSliderMs,
            thumbPositionConverter =dummyProjecter
        )
    }


    @Test
    fun onDrag(){
        val gs by gridSliderMs
        dummyProjecter.i = 55
//        action.onDrag()

        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

        println("=====")
        dummyProjecter.i=200
//        action.onDrag()
        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

        println("=====")
        dummyProjecter.i=1
//        action.onDrag()
        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

    }

}