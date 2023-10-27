package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import javax.inject.Inject

/**
 * TODO Click-on-rail action is still very buggy. Need fixing.
 */
@WsScope
class ClickOnRailAction @Inject constructor(
    private val wsGetter: WorksheetStateGetter,
    private val sliderMs: Ms<GridSlider>,
    val drag:DragThumb,
) {
    val slider by sliderMs
    fun run(data: ScrollBarActionData.ClickOnRail) {
        val (clickPosition: Float, state: ScrollBarState) = data
        drag.run(ScrollBarActionData.Drag(state))
    }
}