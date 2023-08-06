package com.qxdzbc.p6.ui.document.worksheet.di.comp

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddressUtils
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory
import com.qxdzbc.p6.ui.document.worksheet.di.DefaultCellLayoutMap
import com.qxdzbc.p6.ui.document.worksheet.di.comp.ColRuler
import com.qxdzbc.p6.ui.document.worksheet.di.comp.RowRuler
import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsScope
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedSlider
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainers
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import dagger.Binds
import dagger.Provides

import dagger.Module
@Module
interface WorksheetStateModule {


    companion object {

        @Provides
        @WsScope
        fun sliderMs(
            gridSliderFactory: LimitedGridSliderFactory,
        ): Ms<GridSlider> {
            return ms(gridSliderFactory.create())
        }

        @Provides
        @WsScope
        fun cursorStateMs(
            cursorStateFactory: CursorStateFactory,
            @DefaultCellLayoutMap
            cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,
            thumbStateFactory: ThumbStateFactory,
            wsMs: Ms<Worksheet>,
        ): Ms<CursorState> {
            val worksheet = wsMs.value
            val wsIdMs: St<WorksheetId> = worksheet.idMs
            val cursorIdMs: Ms<CursorId> = ms(
                CursorIdImp(wsIdMs)
            )
            val mainCellMs: Ms<CellAddress> = ms(CellAddresses.A1)
            return ms(
                cursorStateFactory.create(
                    idMs = cursorIdMs,
                    cellLayoutCoorsMapSt = cellLayoutCoorMapMs,
                    thumbStateMs = ms(
                        thumbStateFactory.create(
                            cursorIdSt = cursorIdMs,
                            mainCellSt = mainCellMs,
                            cellLayoutCoorMapSt = cellLayoutCoorMapMs,
                        )
                    ),
                    mainCellMs = mainCellMs
                )
            )
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
        @DefaultCellLayoutMap
        fun defaultCellLayoutMap():Ms<Map<CellAddress, LayoutCoorWrapper>>{
            return  ms(emptyMap())
        }

    }
}
