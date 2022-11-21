package com.qxdzbc.p6.app.action.worksheet.action2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.document.cell.address.CellAddress


import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorksheetAction2::class)
class WorksheetAction2Imp @Inject constructor(
    private val mouseOnWsAction: MouseOnWorksheetAction,
    val stateContMs: Ms<StateContainer>,
    private val computeSliderSizeAction: ComputeSliderSizeAction,
    private val makeSliderFollowCellAct: MakeSliderFollowCellAction,
) : WorksheetAction2, MouseOnWorksheetAction by mouseOnWsAction, ComputeSliderSizeAction by computeSliderSizeAction {

    private var sc by stateContMs

    override fun makeSliderFollowCursorMainCell(
        newCursor: CursorState,
        wsLoc: WbWs,
    ) {
        makeSliderFollowCellAct.makeSliderFollowCell(wsLoc,newCursor.mainCell)
    }

    override fun scroll(x: Int, y: Int, wsLoc: WbWsSt) {
        sc.getWsStateMs(wsLoc)?.also { wsStateMs ->
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
                    .setSliderAndRefreshDependentStates(newSlider)
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
        val wsStateMs = sc.getWsStateMs(wsLoc)
        if (wsStateMs != null) {
            wsStateMs.value = wsStateMs.value
                .addCellLayoutCoor(cellAddress, layoutCoordinates.wrap())
        }
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt) {
        sc.getWsStateMs(wsLoc)?.also {
            val wsState by it
            it.value = wsState
                .removeCellLayoutCoor(cellAddress)
        }
    }

    override fun removeAllCellLayoutCoor(wsLoc: WbWsSt) {
        sc.getWsStateMs(wsLoc)?.also {
            val wsState by it
            it.value = wsState
                .removeAllCellLayoutCoor()
        }
    }


    override fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsStateMs = sc.getWsStateMs(wsLoc)
        wsStateMs?.also {
            val wsState by it
            val newState =wsState
                .setCellGridLayoutCoorWrapper(newLayoutCoordinates.wrap())
            it.value = newState
        }
    }

    override fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsStateMs = sc.getWsStateMs(
            wsLoc
        )
        wsStateMs?.also {
            val wsState by it
            it.value = wsState
                .setwsLayoutCoorWrapper(newLayoutCoordinates.wrap())
        }
    }
}
