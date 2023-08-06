package com.qxdzbc.p6.ui.document.worksheet.di.comp

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.worksheet.cursor.di.MainCellMs
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.slider.di.LimitedSliderQualifier
import dagger.Binds
import dagger.Provides

import dagger.Module

@Module
interface WsModule {


    companion object {

        @Provides
        @WsScope
        fun sliderMs(
            @LimitedSliderQualifier
            i:GridSlider,
        ): Ms<GridSlider> {
            return ms(i)
        }


        @Provides
        @WsScope
        fun thumbStateMs(
            thumbState:ThumbState,
        ): Ms<ThumbState> {
            return ms(
                thumbState
            )
        }

        @Provides
        @WsScope
        fun cursorStateMs(i: CursorState): Ms<CursorState> {
            return ms(i)
        }

        @Provides
        @WsScope
        @ColRuler
        fun colRulerStateMs(
            wsMs: Ms<Worksheet>,
            sliderMs: Ms<GridSlider>
        ): Ms<RulerState> = ms(
            RulerStateImp(
                wsIdSt = wsMs.value.idMs,
                type = RulerType.Col,
                sliderMs = sliderMs,
            )
        )

        @Provides
        @WsScope
        @RowRuler
        fun rowRulerStateMs(
            wsMs: Ms<Worksheet>,
            sliderMs: Ms<GridSlider>
        ): Ms<RulerState> = ms(
            RulerStateImp(
                wsIdSt = wsMs.value.idMs,
                type = RulerType.Row,
                sliderMs = sliderMs
            )
        )

        @Provides
        @WsScope
        fun defaultCellLayoutMap(): Ms<Map<CellAddress, LayoutCoorWrapper>> {
            return ms(emptyMap())
        }

    }

    @Binds
    @WsScope
    fun cellLayoutMapSt( i:Ms<Map<CellAddress, LayoutCoorWrapper>>):St<Map<CellAddress, LayoutCoorWrapper>>
}
