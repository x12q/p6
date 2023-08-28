package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.ProjectThumbPosition
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@WsScope
@ContributesBinding(scope= WsAnvilScope::class)
class EdgeSliderActionImp @Inject constructor(
    val sliderMs:Ms<GridSlider>,
    @ForVerticalWsEdgeSlider
    val projectThumbPosition:ProjectThumbPosition,
) : EdgeSliderAction {
    var slider by sliderMs
    override fun onDrag() {
        val edgeRowRange = slider.edgeSliderRowRange
        val newEdgeRow = projectThumbPosition.projectThumbPositionToIndex(edgeRowRange)
        // move the top row to this
        val shiftCount = newEdgeRow - slider.topLeftCell.rowIndex
        sliderMs.value = slider.shiftDown(shiftCount)
    }
}