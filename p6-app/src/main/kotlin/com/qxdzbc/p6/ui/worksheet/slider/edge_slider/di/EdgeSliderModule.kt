package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di

import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderStateImp
import dagger.Binds
import dagger.Module
import javax.inject.Qualifier


@Qualifier
annotation class VerticalWsEdgeSliderStateQualifier

@Qualifier
annotation class HorizontalWsEdgeSliderStateQualifier

@Module
interface EdgeSliderModule {

    @WsScope
    @Binds
    @VerticalWsEdgeSliderStateQualifier
    fun VerticalEdgeSliderState(i:EdgeSliderStateImp):EdgeSliderState


    @WsScope
    @Binds
    @HorizontalWsEdgeSliderStateQualifier
    fun HorizontalEdgeSlider(i:EdgeSliderStateImp):EdgeSliderState
}