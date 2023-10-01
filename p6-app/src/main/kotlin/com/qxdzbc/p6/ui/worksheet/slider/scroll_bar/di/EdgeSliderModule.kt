package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import dagger.Binds
import dagger.Module


@Module
interface EdgeSliderModule {

    @Binds
    @ForVerticalWsEdgeSlider
    fun thumpPositionConverterVertical(@ForVerticalWsEdgeSlider i:ScrollBarState):ThumbPositionConverter

}