package com.qxdzbc.p6.ui.worksheet.slider.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.EdgeSliderModule
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        EdgeSliderModule::class
    ]
)
interface SliderModule {

    companion object{
        @Provides
        @WsScope
        fun gridSliderMs(i: GridSlider): Ms<GridSlider> {
            return StateUtils.ms(i)
        }

        @Provides
        @WsScope
        fun gridSliderSt(i:Ms<GridSlider>): St<GridSlider> {
            return i
        }
    }
}