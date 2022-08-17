package com.emeraldblast.p6.ui.document.worksheet.state

import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.St
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorIdImp
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorState
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateFactory
import com.emeraldblast.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerState
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerStateImp
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerType
import com.emeraldblast.p6.ui.document.worksheet.slider.GridSlider
import com.emeraldblast.p6.ui.document.worksheet.slider.LimitedGridSliderFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface WorksheetStateFactory {

    fun create(
        @Assisted("1") wsMs: Ms<Worksheet>,
        @Assisted("2") sliderMs: Ms<GridSlider>,
        @Assisted("3") cursorStateMs: Ms<CursorState>,
        // ============================================//
        @Assisted("4")
        colRulerStateMs: Ms<RulerState> = ms(
            RulerStateImp(
                wsIdSt = wsMs.value.idMs,
                dimen = RulerType.Col,
                sliderMs = sliderMs,

                )
        ),
        @Assisted("5")
        rowRulerStateMs: Ms<RulerState> = ms(
            RulerStateImp(
                wsIdSt = wsMs.value.idMs,
                dimen = RulerType.Row,
                sliderMs = sliderMs
            )
        ),
    ): WorksheetStateImp

    companion object {
        fun WorksheetStateFactory.createRefresh(
            worksheetMs: Ms<Worksheet>,
            sliderMs: Ms<GridSlider>,
            cursorStateMs: Ms<CursorState>,
        ): WorksheetState {
            return this.create(
                worksheetMs, sliderMs, cursorStateMs).refreshCellState()
        }

        fun WorksheetStateFactory.createRefresh(
            worksheetMs: Ms<Worksheet>,
            gridSliderFactory: LimitedGridSliderFactory,
            cursorStateFactory: CursorStateFactory,
        ): WorksheetState {
            val worksheet = worksheetMs.value
            val wsIdMs: St<WorksheetId> = worksheet.idMs
            val cursorAddress: Ms<CursorStateId> = ms(
                CursorIdImp(wsIdMs)
            )
            val wsState = createRefresh(
                worksheetMs = worksheetMs,
                sliderMs = ms(gridSliderFactory.create()),
                cursorStateMs = ms(
                    cursorStateFactory.create(
                        idMs = cursorAddress,
                    )
                )
            )
            return wsState
        }
    }
}
