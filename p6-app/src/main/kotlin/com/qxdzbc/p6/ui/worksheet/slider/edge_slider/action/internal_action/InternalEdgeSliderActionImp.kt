package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.EdgeSliderActionType
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.ProjectThumbPosition
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class InternalEdgeSliderActionImp @Inject constructor(
    val sliderMs: Ms<GridSlider>,
    @ForVerticalWsEdgeSlider
    val projectThumbPosition: ProjectThumbPosition,
    val stateCont:StateContainer
) : InternalEdgeSliderAction {

    var slider by sliderMs

    override fun run(data: EdgeSliderActionType.Drag) {
        val edgeRowRange = slider.edgeSliderRowRange
        val newEdgeRow = projectThumbPosition.projectThumbPositionToIndex(edgeRowRange)
        // move the top row to this
        val shiftCount = newEdgeRow - slider.topLeftCell.rowIndex
        sliderMs.value = slider.shiftDown(shiftCount)
    }
}