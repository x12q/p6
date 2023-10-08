package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForHorizontalScrollBar
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalScrollBar
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import dagger.Binds
import dagger.Module


@Module
interface EdgeSliderModule {

    @Binds
    @ForVerticalScrollBar
    fun thumpPositionConverterVertical(@ForVerticalScrollBar i:ScrollBarState):ThumbPositionConverter

    @Binds
    @ForHorizontalScrollBar
    fun thumpPositionConverterVerticalHorizontal(@ForHorizontalScrollBar i:ScrollBarState):ThumbPositionConverter

}