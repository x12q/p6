package com.qxdzbc.p6.ui.document.worksheet.state

import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.state.ThumbStateFactory
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

//@AssistedFactory
interface WorksheetStateInternalFactory {

//    fun create(
//        @Assisted("1") wsMs: Ms<Worksheet>,
//        @Assisted("2") sliderMs: Ms<GridSlider>,
//        @Assisted("3") cursorStateMs: Ms<CursorState>,
//        // ============================================//
//        @Assisted("4")
//        colRulerStateMs: Ms<RulerState> = ms(
//            RulerStateImp(
//                wsIdSt = wsMs.value.idMs,
//                type = RulerType.Col,
//                sliderMs = sliderMs,
//
//                )
//        ),
//        @Assisted("5")
//        rowRulerStateMs: Ms<RulerState> = ms(
//            RulerStateImp(
//                wsIdSt = wsMs.value.idMs,
//                type = RulerType.Row,
//                sliderMs = sliderMs
//            )
//        ),
//        @Assisted("6") cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>
//    ): WorksheetStateImp

    companion object {
        /**
         * Create a worksheet state and refresh it immediately
         */
//        fun WorksheetStateInternalFactory.createThenRefresh(
//            wsMs: Ms<Worksheet>,
//            sliderMs: Ms<GridSlider>,
//            cursorStateMs: Ms<CursorState>,
//            cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>>,
//        ): WorksheetState {
//            return this.create(
//                wsMs=wsMs,
//                sliderMs=sliderMs,
//                cursorStateMs=cursorStateMs,
//                cellLayoutCoorMapMs = cellLayoutCoorMapMs
//            ).apply {
//                refresh()
//            }
//        }
//
//        /**
//         * Create a worksheet state and refresh it immediately
//         */
//        fun WorksheetStateInternalFactory.createThenRefresh(
//            wsMs: Ms<Worksheet>,
//            gridSliderFactory: LimitedGridSliderFactory,
//            cursorStateFactory: CursorStateFactory,
//            thumbStateFactory: ThumbStateFactory,
//        ): WorksheetState {
//            val worksheet = wsMs.value
//            val wsIdMs: St<WorksheetId> = worksheet.idMs
//            val cursorIdMs: Ms<CursorId> = ms(
//                CursorIdImp(wsIdMs)
//            )
//            val cellLayoutCoorMapMs: Ms<Map<CellAddress, LayoutCoorWrapper>> = ms(emptyMap())
//            val mainCellMs:Ms<CellAddress> = ms(CellAddresses.A1)
//            val wsState:WorksheetState = createThenRefresh(
//                wsMs = wsMs,
//                sliderMs = ms(gridSliderFactory.create()),
//                cursorStateMs = ms(
//                    cursorStateFactory.create(
//                        idMs = cursorIdMs,
//                        cellLayoutCoorsMapSt = cellLayoutCoorMapMs,
//                        thumbStateMs = ms(thumbStateFactory.create(
//                            cursorIdSt = cursorIdMs,
//                            mainCellSt = mainCellMs,
//                            cellLayoutCoorMapSt = cellLayoutCoorMapMs,
//                        )),
//                        mainCellMs = mainCellMs
//                    )
//                ),
//                cellLayoutCoorMapMs=cellLayoutCoorMapMs,
//            )
//            return wsState
//        }
    }
}
