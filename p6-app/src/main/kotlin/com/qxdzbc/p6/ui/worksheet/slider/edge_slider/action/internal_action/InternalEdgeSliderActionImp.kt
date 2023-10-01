package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.EdgeSliderActionType
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.ThumbPositionConverter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class InternalEdgeSliderActionImp @Inject constructor(
    val sliderMs: Ms<GridSlider>,
    @ForVerticalWsEdgeSlider
    val projectThumbPosition: ThumbPositionConverter,
) : InternalEdgeSliderAction {

    var slider by sliderMs

    override fun drag(data: EdgeSliderActionType.Drag) {
        val edgeRowRange = slider.edgeSliderRowRange
        val newEdgeRow = projectThumbPosition.convertThumbPositionToIndex(edgeRowRange)
        // move the top row to this
        val shiftCount = newEdgeRow - slider.topLeftCell.rowIndex
        sliderMs.value = slider.shiftDown(shiftCount)
    }
}