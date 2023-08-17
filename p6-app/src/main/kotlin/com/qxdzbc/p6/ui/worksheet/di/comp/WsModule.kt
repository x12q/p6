package com.qxdzbc.p6.ui.worksheet.di.comp

import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.common.compose.layout_coor_wrapper.P6LayoutCoor
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.command.CommandStack
import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.ui.workbook.di.DefaultCommandStackMs
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.worksheet.di.qualifiers.*
import com.qxdzbc.p6.ui.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.worksheet.resize_bar.ResizeBarStateImp
import com.qxdzbc.p6.ui.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.worksheet.ruler.RulerStateImp
import com.qxdzbc.p6.ui.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.state.CellStateContainer
import com.qxdzbc.p6.ui.worksheet.state.CellStateContainers
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import com.qxdzbc.p6.ui.worksheet.slider.di.SliderModule
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.EdgeSliderModule
import dagger.Binds
import dagger.Provides

import dagger.Module

@Module(
    includes = [
        EdgeSliderModule::class,
        SliderModule::class,
    ]
)
interface WsModule {

    @Binds
    @WsScope
    fun cellLayoutMapSt(i: Ms<Map<CellAddress, P6LayoutCoor>>): St<Map<CellAddress, P6LayoutCoor>>


    @Binds
    @WsScope
    @WsUndoStack
    fun undoStackMs(@DefaultCommandStackMs i: Ms<CommandStack>): Ms<CommandStack>

    @Binds
    @WsScope
    @WsRedoStack
    fun redoStackMs(@DefaultCommandStackMs i: Ms<CommandStack>): Ms<CommandStack>

    companion object {

        @Provides
        @WsScope
        fun thumbStateMs(
            thumbState: ThumbState,
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
        fun defaultCellLayoutMap(): Ms<Map<CellAddress, P6LayoutCoor>> {
            return ms(emptyMap())
        }

        @Provides
        @WsScope
        @DefaultVisibleRowRange
        fun DefaultVisibleRowRange(): IntRange {
            return WorksheetConstants.defaultVisibleRowRange
        }

        @Provides
        @DefaultVisibleColRange
        @WsScope
        fun DefaultVisibleColRange(): IntRange {
            return WorksheetConstants.defaultVisibleColRange
        }

        @Provides
        @DefaultCellStateContainer
        @WsScope
        fun DefaultCellStateContainer(): Ms<CellStateContainer> {
            return CellStateContainers.immutable().toMs()
        }



        @Provides
        @WsScope
        @DefaultColResizeBarStateMs
        fun ResizeColBarStateMs(): Ms<ResizeBarState> {
            return ms(
                ResizeBarStateImp(
                    rulerType = RulerType.Col,
                    thumbSize = WorksheetConstants.defaultRowHeight
                )
            )
        }

        @Provides
        @WsScope
        @DefaultRowResizeBarStateMs
        fun ResizeRowBarStateMs(): Ms<ResizeBarState> {
            return ms(
                ResizeBarStateImp(
                    rulerType = RulerType.Row,
                    thumbSize = WorksheetConstants.rowRulerWidth
                )
            )
        }

        @Provides
        @WsScope
        fun cellFormatTableMs(): Ms<CellFormatTable> = ms(CellFormatTableImp())

        @Provides
        @WsScope
        @CellGridLayoutMs
        fun cellGridLayoutCoorWrapperMs(): Ms<P6LayoutCoor?> = ms(null)

        @Provides
        @WsScope
        @WsLayoutMs
        fun wsLayoutCoorWrapperMs(): Ms<P6LayoutCoor?> = ms(null)

        @Provides
        @WsScope
        @Init_ColRange
        fun colRange(): IntRange = WorksheetConstants.defaultColRange

        @Provides
        @WsScope
        @Init_RowRange
        fun rowRange(): IntRange = WorksheetConstants.defaultRowRange
    }
}

