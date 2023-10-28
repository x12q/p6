package com.qxdzbc.p6.ui.worksheet.slider.action.make_scroll_bar_reflect_slider

import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState

/**
 * This reads a [GridSlider] and make a [ScrollBarState] reflect the state of the [GridSlider]
 */
interface MakeScrollBarReflectSlider{
    fun reflect(
        scrollBarState: ScrollBarState,
        slider: GridSlider,
        easingFactor: Int = 15,
        isIncreasing:Boolean
    )
}