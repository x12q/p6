package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import test.BaseAppStateTest
import kotlin.test.*

class DragThumbActionTest: BaseAppStateTest(){
    lateinit var action: DragThumb
    lateinit var gridSliderMs:Ms<GridSlider>

    object dummyConverter: ThumbPositionConverter {
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
        gridSliderMs = StateUtils.ms(GridSliderImp.forPreview())
        action = DragThumb(
            wsGetter = updateSlider,
            sliderMs = gridSliderMs,
            thumbPositionConverterForVerticalScrollBar = dummyConverter,
            thumbPositionConverterForHorizontalScrollBar = dummyConverter,
        )
    }


    @Test
    fun onDrag(){
//        val gs by gridSliderMs
//        dummyConverter.i = 55
//
//        println(gs.visibleRowRangeIncludeMargin)
//        println(gs.scrollBarRowRange)
//
//        println("=====")
//        dummyConverter.i =200
//        println(gs.visibleRowRangeIncludeMargin)
//        println(gs.scrollBarRowRange)
//
//        println("=====")
//        dummyConverter.i =1
//        println(gs.visibleRowRangeIncludeMargin)
//        println(gs.scrollBarRowRange)

    }
}
