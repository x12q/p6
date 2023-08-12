package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderStateImp
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface EdgeSliderModule {


    @WsScope
    @Binds
    fun VerticalEdgeSliderState(i:VerticalEdgeSliderStateImp):VerticalEdgeSliderState

    companion object{
//        @Provides
//        @WsScope
//        fun verticalEdgeSliderStateMs():Ms<VerticalEdgeSliderState>{
//
//        }
    }
}