package com.qxdzbc.p6.ui.worksheet.slider.action.make_slider_follow_cell

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.di.P6AnvilScope

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouters.publishErrIfNeedSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.action.make_scroll_bar_reflect_slider.MakeScrollBarReflectSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class MoveSliderActionImp @Inject constructor(
    val stateCont:StateContainer,
    val errorRouter: ErrorRouter,
    val makeScrollBarReflectSlider: MakeScrollBarReflectSlider,
) : MoveSliderAction {

    private val sc  = stateCont

    override fun makeSliderFollowCell(wbws: WbWs, cellAddr: CellAddress, publishErr: Boolean): Rse<Unit> {
        return sc.getWbWsStRs(wbws).map {
            this.makeSliderFollowCell(it,cellAddr, publishErr)
        }
    }

    override fun makeSliderFollowCell(wbwsSt: WbWsSt, cellAddr: CellAddress, publishErr: Boolean): Rse<Unit> {
        val rs=sc.getWsStateRs(wbwsSt).flatMap { wsState->
            val sliderMs = wsState.sliderMs
            val oldSlider = sliderMs.value
            val newSlider = oldSlider.followCell(cellAddr)
                .expandScrollBarLimitIfNeed()
                .shrinkScrollBarLimitIfNeed()
            if (newSlider != oldSlider) {

                wsState.updateSliderAndRefreshDependentStates(newSlider)

                if(newSlider.visibleColRangeIncludeMargin != oldSlider.visibleColRangeIncludeMargin){
                    makeScrollBarReflectSlider.reflectAll(wsState.horizontalScrollBarState,newSlider, easingFactor = 15, reductionRate = 0.0008f)
                }

                if(newSlider.visibleRowRangeIncludeMargin!=oldSlider.visibleRowRangeIncludeMargin){
                    makeScrollBarReflectSlider.reflectAll(wsState.verticalScrollBarState,newSlider,easingFactor = 15,reductionRate = 0.0008f)
                }
            }
            Ok(Unit)
        }
        if(publishErr){
            rs.publishErrIfNeedSt(errorRouter,null,wbwsSt.wbKeySt)
        }
        return rs
    }

    override fun makeSliderFollowCursorMainCell(cursorState: CursorState, wsLoc: WbWsSt) {
        makeSliderFollowCell(wsLoc,cursorState.mainCell)
    }

    override fun makeSliderFollowCursorMainCell(cursorState: CursorState, wsLoc: WbWs) {
        makeSliderFollowCell(wsLoc,cursorState.mainCell)
    }

    override fun shiftSlider(cursorLoc: WbWsSt, rowCount: Int, colCount: Int, publishErr: Boolean) {
        val rs=sc.getWsStateRs(cursorLoc).flatMap { wsState->
            val sliderMs = wsState.sliderMs
            val oldSlider = sliderMs.value
            val newSlider = oldSlider.shiftDown(rowCount).shiftRight(colCount)
            if (newSlider != oldSlider) {
                wsState.updateSliderAndRefreshDependentStates(newSlider)
            }
            Ok(Unit)
        }
        if(publishErr){
            rs.publishErrIfNeedSt(errorRouter,null,cursorLoc.wbKeySt)
        }
    }
}
