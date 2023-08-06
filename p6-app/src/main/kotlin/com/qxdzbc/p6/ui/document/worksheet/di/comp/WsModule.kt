package com.qxdzbc.p6.ui.document.worksheet.di.comp

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.ui.document.workbook.di.DefaultCommandStackMs
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbState
import com.qxdzbc.p6.ui.document.worksheet.di.*
import com.qxdzbc.p6.ui.document.worksheet.di.qualifiers.*
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.slider.di.LimitedSliderQualifier
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainers
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.CellFormatTableImp
import dagger.Binds
import dagger.Provides

import dagger.Module

@Module
interface WsModule {

    @Binds
    @WsScope
    fun cellLayoutMapSt(i: Ms<Map<CellAddress, LayoutCoorWrapper>>): St<Map<CellAddress, LayoutCoorWrapper>>


//    @Binds
//    @WsScope
//    @DefaultSelectRectState
//    fun SelectRectState(i:SelectRectStateImp): SelectRectState

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
        fun sliderMs(
            @LimitedSliderQualifier
            i: GridSlider,
        ): Ms<GridSlider> {
            return ms(i)
        }


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
        fun defaultCellLayoutMap(): Ms<Map<CellAddress, LayoutCoorWrapper>> {
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
        fun cellGridLayoutCoorWrapperMs(): Ms<LayoutCoorWrapper?> = ms(null)

        @Provides
        @WsScope
        @WsLayoutMs
        fun wsLayoutCoorWrapperMs(): Ms<LayoutCoorWrapper?> = ms(null)

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

