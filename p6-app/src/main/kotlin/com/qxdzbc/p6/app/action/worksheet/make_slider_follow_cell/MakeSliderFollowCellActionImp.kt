package com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.cell.address.CellAddress

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.error_router.ErrorRouters.publishErrIfNeedSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import javax.inject.Inject

class MakeSliderFollowCellActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    val errorRouter: ErrorRouter,
) : MakeSliderFollowCellAction {

    private val sc by stateContSt

    override fun makeSliderFollowCell(cursorLoc: WbWs, cell: CellAddress, publishErr: Boolean): Rse<Unit> {
        return sc.getWbWsStRs(cursorLoc).map {
            this.makeSliderFollowCell(it,cell, publishErr)
        }
    }

    override fun makeSliderFollowCell(cursorLoc: WbWsSt, cell: CellAddress, publishErr: Boolean): Rse<Unit> {
        val rs=sc.getWsStateMsRs(cursorLoc).flatMap {wsStateMs->
            val sliderMs = wsStateMs.value.sliderMs
            val oldSlider = sliderMs.value
            val newSlider = oldSlider.followCell(cell)
            if (newSlider != oldSlider) {
                wsStateMs.value = wsStateMs.value.setSliderAndRefreshDependentStates(newSlider)
            }
            Ok(Unit)
        }
        if(publishErr){
            rs.publishErrIfNeedSt(errorRouter,null,cursorLoc.wbKeySt)
        }
        return rs
    }


}
