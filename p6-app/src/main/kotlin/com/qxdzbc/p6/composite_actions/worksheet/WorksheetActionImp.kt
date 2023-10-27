package com.qxdzbc.p6.composite_actions.worksheet

import androidx.compose.ui.layout.LayoutCoordinates
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6Layout
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.composite_actions.range.range_to_clipboard.RangeToClipboardAction
import com.qxdzbc.p6.composite_actions.worksheet.compute_slider_size.ComputeSliderSizeAction
import com.qxdzbc.p6.composite_actions.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.ui.worksheet.slider.action.make_slider_follow_cell.MoveSliderAction
import com.qxdzbc.p6.composite_actions.worksheet.mouse_on_ws.MouseOnWorksheetAction
import com.qxdzbc.p6.composite_actions.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
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
    private val moveSliderAct: MoveSliderAction,
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

    override fun onMouseScroll(x: Int, y: Int, wsLoc: WbWsSt) {
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
                wsState.updateSliderAndRefreshDependentStates(newSlider)
            }
        }
    }

    override fun addCellLayoutCoor(
        cellAddress: CellAddress,
        layoutCoordinates: LayoutCoordinates,
        wsLoc: WbWsSt
    ) {
        sc.getWsState(wsLoc)?.addCellLayoutCoor(cellAddress, layoutCoordinates.toP6Layout())
    }

    override fun removeCellLayoutCoor(cellAddress: CellAddress, wsLoc: WbWsSt) {
        sc.getWsState(wsLoc)?.removeCellLayoutCoor(cellAddress)
    }

    override fun removeAllCellLayoutCoor(wsLoc: WbWsSt) {
        sc.getWsState(wsLoc)?.removeAllCellLayoutCoor()
    }

    override fun updateCellGridLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsState = sc.getWsState(wsLoc)
        wsState?.setCellGridLayoutCoorWrapper(newLayoutCoordinates.toP6Layout())
    }

    override fun updateWsLayoutCoors(newLayoutCoordinates: LayoutCoordinates, wsLoc: WbWsSt) {
        val wsState = sc.getWsState(
            wsLoc
        )
        wsState?.setWsLayoutCoorWrapper(newLayoutCoordinates.toP6Layout())
    }
}
