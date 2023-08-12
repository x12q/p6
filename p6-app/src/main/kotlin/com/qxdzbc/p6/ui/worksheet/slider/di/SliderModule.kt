package com.qxdzbc.p6.ui.worksheet.slider.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.LimitedSlider
import com.qxdzbc.p6.ui.worksheet.slider.di.qualifiers.LimitedSliderQualifier
import com.qxdzbc.p6.ui.worksheet.slider.di.qualifiers.LimitedSliderQualifierSt
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface SliderModule {



    companion object{
        @Provides
        @WsScope
        fun GridSliderMs(i:Ms<LimitedSlider>):Ms<GridSlider>{
            val q:Ms<GridSlider> = i as Ms<GridSlider>
            return q
        }
        @Provides
        @WsScope
        fun LimitedSliderMs(
            @LimitedSliderQualifier
            i: LimitedSlider,
        ): Ms<LimitedSlider> {
            return StateUtils.ms(i)
        }

        @Provides
        @WsScope
        @LimitedSliderQualifierSt
        fun LimitedSliderSt(i:Ms<LimitedSlider>): St<LimitedSlider> {
            return i
        }


    }
}