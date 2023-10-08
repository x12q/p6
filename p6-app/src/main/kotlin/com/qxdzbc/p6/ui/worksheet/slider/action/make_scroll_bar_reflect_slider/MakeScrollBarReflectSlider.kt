package com.qxdzbc.p6.ui.worksheet.slider.action.make_scroll_bar_reflect_slider

import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState

/**
 * This reads a [GridSlider] and make a [ScrollBarState] reflect the state of the [GridSlider]
 */
interface MakeScrollBarReflectSlider{
    fun reflectAll(
        scrollBarState: ScrollBarState,
        slider: GridSlider,
        easingFactor: Int = 15,
        isIncreasing:Boolean
    )

    fun reflectPosition(
        scrollBarState: ScrollBarState,
        slider: GridSlider
    )

    /**
     * make thumb size of [scrollBarState] reflect the state of grid slider.
     * The more col/row is scrolled, the smaller the thumb. And vise versa.
     *
     * [easingFactor] is to prevent the thumb from shrinking too fast. The bigger the [easingFactor], the slower it shrinks. Must be non-zero.
     *
     * [reductionRate] is how much the thumb length should be reduced based on the current length. Must be between [0,1]
     */
    fun reflectThumbSize(
        scrollBarState: ScrollBarState,
        slider: GridSlider,
        easingFactor: Int = 15,
        reductionRate: Float = 0.1f,
    )
}