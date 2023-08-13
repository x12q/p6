package com.qxdzbc.p6.ui.worksheet.slider.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import dagger.Module
import dagger.Provides

@Module
interface SliderModule {

    companion object{
        @Provides
        @WsScope
        fun LimitedSliderMs(i: GridSlider): Ms<GridSlider> {
            return StateUtils.ms(i)
        }

        @Provides
        @WsScope
        fun LimitedSliderSt(i:Ms<GridSlider>): St<GridSlider> {
            return i
        }
    }
}