package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.InternalScrollBarActionImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import test.BaseAppStateTest
import kotlin.test.*



class ScrollBarActionImpTest : BaseAppStateTest(){
    lateinit var action: InternalScrollBarActionImp
    lateinit var gridSliderMs:Ms<GridSlider>

    object dummyProjecter: ThumbPositionConverter{
        var i:Int = 1
        override fun convertThumbPositionToIndex(indexRange: IntRange): Int {
            return i
        }
    }

    val updateSlider = WorksheetStateGetter{
        ts.sc.getWsState(ts.wb1Ws1St)
    }

    @BeforeTest
    fun bt(){
        gridSliderMs = ms(GridSliderImp.forPreview())
        action = InternalScrollBarActionImp(
            wsGetter = updateSlider,
            sliderMs = gridSliderMs,
            thumbPositionConverterForVerticalScrollBar = dummyProjecter,
            thumbPositionConverterForHorizontalScrollBar = dummyProjecter,
        )
    }


    @Test
    fun onDrag(){
        val gs by gridSliderMs
        dummyProjecter.i = 55

        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

        println("=====")
        dummyProjecter.i=200
        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

        println("=====")
        dummyProjecter.i=1
        println(gs.visibleRowRangeIncludeMargin)
        println(gs.edgeSliderRowRange)

    }

}