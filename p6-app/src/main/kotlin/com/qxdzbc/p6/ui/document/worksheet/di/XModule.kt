package com.qxdzbc.p6.ui.document.worksheet.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddressUtils
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.document.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.document.worksheet.di.*
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainers
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import dagger.Binds
import dagger.Module
import dagger.Provides


/**
 * Contain mostly obj for cursor state
 *
 *
 */
@Module
interface XModule {

    companion object{

        @Provides
        @DefaultVisibleRowRange
        fun DefaultVisibleRowRange():IntRange{
            return WorksheetConstants.defaultVisibleRowRange
        }

        @Provides
        @DefaultVisibleColRange
        fun DefaultVisibleColRange():IntRange{
            return WorksheetConstants.defaultVisibleColRange
        }

        @Provides
        @DefaultCellStateContainer
        fun DefaultCellStateContainer(): Ms<CellStateContainer> {
            return CellStateContainers.immutable().toMs()
        }

        @Provides
        @DefaultSelectRectState
        fun SelectRectState(): SelectRectState {
            return SelectRectStateImp()
        }

        @Provides
        @DefaultSelectRectStateMs
        fun SelectRectStateMs(@DefaultSelectRectState i: SelectRectState): Ms<SelectRectState> {
            return StateUtils.ms(i)
        }

        @Provides
        @DefaultColResizeBarStateMs
        fun ResizeColBarStateMs(): Ms<ResizeBarState> {
            return StateUtils.ms(
                ResizeBarStateImp(
                    rulerType = RulerType.Col,
                    thumbSize = WorksheetConstants.defaultRowHeight
                )
            )
        }

        @Provides
        @DefaultRowResizeBarStateMs
        fun ResizeRowBarStateMs(): Ms<ResizeBarState> {
            return StateUtils.ms(
                ResizeBarStateImp(
                    rulerType = RulerType.Row,
                    thumbSize = WorksheetConstants.rowRulerWidth
                )
            )
        }

        @Provides
        @DefaultRangeConstraint
        fun DefaultRangeConstraint(): RangeConstraint {
            return WorksheetConstants.defaultRangeConstraint
        }

        @Provides
        @DefaultClipBoardRange
        fun clipboardRange(): RangeAddress {
            return RangeAddressUtils.InvalidRange
        }


        @Provides
        @EmptyCellAddressSet
        fun ecSet(): Set<CellAddress> {
            return emptySet()
        }

        @Provides
        @EmptyRangeAddressSet
        fun erSet(): Set<RangeAddress> {
            return emptySet()
        }

        @Provides
        @NullRangeAddress
        fun nullRangeAddress(): RangeAddress?{
            return null
        }

    }
}