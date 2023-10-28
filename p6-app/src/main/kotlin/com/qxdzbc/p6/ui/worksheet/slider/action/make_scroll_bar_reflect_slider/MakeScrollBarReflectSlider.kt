package com.qxdzbc.p6.ui.worksheet.slider.action.make_scroll_bar_reflect_slider

import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState

/**
 * This reads a [GridSlider] and make a [ScrollBarState] reflect the state of the [GridSlider]
 */
interface MakeScrollBarReflectSlider{
    /**
     * Reflect a [GridSlider] state onto a [ScrollBarState], this include:
     * - the data in [newSlider]
     * - the transition from [oldSlider] to [newSlider]
     */
    fun reflect(
        scrollBarState: ScrollBarState,
        oldSlider: GridSlider,
        newSlider: GridSlider,
    )

    /**
     * Recompute the thumb position of [scrollBarState] based on the data in [slider]
     */
    fun reflectThumbPosition(scrollBarState: ScrollBarState, slider: GridSlider)
}