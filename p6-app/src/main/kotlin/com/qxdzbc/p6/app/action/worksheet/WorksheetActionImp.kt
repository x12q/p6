package com.qxdzbc.p6.app.action.worksheet

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardAction
import com.qxdzbc.p6.app.action.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MoveSliderAction
import com.qxdzbc.p6.app.action.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class,boundType =WorksheetAction::class)
class WorksheetActionImp @Inject constructor(
    private val rangeToClipboardAction: RangeToClipboardAction,
    private val deleteMultiAct: DeleteMultiCellAction,
    private val restoreWindowFocusState: RestoreWindowFocusState,
    val stateCont: StateContainer,
    private val makeSliderFollowCellAct: MoveSliderAction,
    private val computeSliderSizeAction: ComputeSliderSizeAction,
    private val mouseOnWsAction: MouseOnWorksheetAction,
) : WorksheetAction,
    RangeToClipboardAction by rangeToClipboardAction,
    DeleteMultiCellAction by deleteMultiAct,
    RestoreWindowFocusState by restoreWindowFocusState,
    ComputeSliderSizeAction by computeSliderSizeAction,
    MouseOnWorksheetAction by mouseOnWsAction
{

    private val sc = stateCont

    override fun makeSliderFollowCursorMainCell(newCursor: CursorState, wsLoc: WbWsSt) {
        makeSliderFollowCellAct.makeSliderFollowCell(wsLoc,newCursor.mainCell)
    }

    override fun makeSliderFollowCursorMainCell(
        newCursor: CursorState,
        wsLoc: WbWs,
    ) {
        makeSliderFollowCellAct.makeSliderFollowCell(wsLoc,newCursor.mainCell)
    }

    override fun scroll(x: Int, y: Int, wsLoc: WbWsSt) {
        sc.getWsState(wsLoc)?.also { wsState ->
            val sliderState = wsState.slider
            var newSlider = sliderState
            if (x != 0) {
                newSlider = newSlider.shiftRight(x)
            }
            if (y != 0) {
                newSlider = newSlider.shiftDown(y)
            }
            if (newSlider != sliderState) {
                wsState
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
        sc.getWsState(wsLoc)?.addCellLayoutCoor(cellAddress, layoutCoordinates.wrap())
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt) {
        sc.getWsState(wsLoc)?.removeCellLayoutCoor(cellAddress)
    }

    override fun removeAllCellLayoutCoor(wsLoc: WbWsSt) {
        sc.getWsState(wsLoc)?.removeAllCellLayoutCoor()
    }


    override fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsState = sc.getWsState(wsLoc)
        wsState?.setCellGridLayoutCoorWrapper(newLayoutCoordinates.wrap())
    }

    override fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsState = sc.getWsState(
            wsLoc
        )
        wsState?.setWsLayoutCoorWrapper(newLayoutCoordinates.wrap())
    }
}
