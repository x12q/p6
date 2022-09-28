package com.qxdzbc.p6.app.action.worksheet.action2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

class WorksheetAction2Imp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val mouseOnWsAction: MouseOnWorksheetAction,
    @StateContainerMs val stateContMs: Ms<StateContainer>,
    val computeSliderSizeAction: ComputeSliderSizeAction
) : WorksheetAction2, MouseOnWorksheetAction by mouseOnWsAction, ComputeSliderSizeAction by computeSliderSizeAction {

    private var stateCont by stateContMs
    private var appState by appStateMs

    override fun makeSliderFollowCursor(
        newCursor: CursorState,
        wsLoc: WbWs,
    ) {
        val wsStateMs: Ms<WorksheetState>? = appState.getWsStateMs(wsLoc)
        if (wsStateMs != null) {

            val oldSlider = wsStateMs.value.slider
            val newSlider = oldSlider.followCursor(newCursor)
            if (newSlider != oldSlider) {
                val colRulerStateMs: Ms<RulerState> = wsStateMs.value.colRulerStateMs
                val rowRulerStateMs: Ms<RulerState> = wsStateMs.value.rowRulerStateMs
                val wsState by wsStateMs
                val colRulerState by colRulerStateMs
                val rowRulerState by rowRulerStateMs

                wsStateMs.value = wsState
//                    .setTopLeftCell(newSlider.topLeftCell)
                    .setSlider(newSlider)
                // x: clear all cached layout coors whenever slider moves to prevent memory from overflowing.
                this.removeAllCellLayoutCoor(wsState)

                colRulerStateMs.value = colRulerState
                    .clearItemLayoutCoorsMap()
                    .clearResizerLayoutCoorsMap()

                rowRulerStateMs.value = rowRulerState
                    .clearItemLayoutCoorsMap()
                    .clearResizerLayoutCoorsMap()
            }
        }
    }

    override fun scroll(x: Int, y: Int, wsLoc: WbWsSt) {
        stateCont.getWsStateMs(wsLoc)?.also { wsStateMs ->
            val wsState by wsStateMs
            val sliderState = wsState.slider
            var newSlider = sliderState
            if (x != 0) {
                newSlider = newSlider.shiftRight(x)
            }
            if (y != 0) {
                newSlider = newSlider.shiftDown(y)
            }
            if (newSlider != sliderState) {
                wsStateMs.value = wsState
//                    .setTopLeftCell(newSlider.topLeftCell)
                    .setSlider(newSlider)
                wsState.cellLayoutCoorMapMs.value =
                    wsState.cellLayoutCoorMap.filter { (cellAddress, _) -> sliderState.containAddress(cellAddress) }
            }
        }
    }

    override fun addCellLayoutCoor(
        cellAddress: CellAddress,
        layoutCoordinates: LayoutCoordinates,
        wsLoc: WbWsSt
    ) {
        val wsStateMs = appState.getWsStateMs(wsLoc)
        if (wsStateMs != null) {
            wsStateMs.value = wsStateMs.value
                .addCellLayoutCoor(cellAddress, layoutCoordinates.wrap())
        }
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt) {
        appState.getWsStateMs(wsLoc)?.also {
            val wsState by it
            it.value = wsState
                .removeCellLayoutCoor(cellAddress)
        }
    }

    override fun removeAllCellLayoutCoor(wsLoc: WbWsSt) {
        appState.getWsStateMs(wsLoc)?.also {
            val wsState by it
            it.value = wsState
                .removeAllCellLayoutCoor()
        }
    }


    override fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsStateMs = appState.getWsStateMs(wsLoc)
        wsStateMs?.also {
            val wsState by it
            val newState =wsState
                .setCellGridLayoutCoorWrapper(newLayoutCoordinates.wrap())
            it.value = newState
        }
    }

    override fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsStateMs = appState.getWsStateMs(
            wsLoc
        )
        wsStateMs?.also {
            val wsState by it
            it.value = wsState
                .setwsLayoutCoorWrapper(newLayoutCoordinates.wrap())
        }
    }
}
