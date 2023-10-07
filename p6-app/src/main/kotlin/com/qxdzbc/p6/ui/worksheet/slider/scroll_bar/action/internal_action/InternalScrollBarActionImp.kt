package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionType
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForHorizontalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class InternalScrollBarActionImp @Inject constructor(
    private val wsGetter: WorksheetStateGetter,
    private val sliderMs: Ms<GridSlider>,
    @ForVerticalWsEdgeSlider
    private val thumbPositionConverterForVerticalScrollBar: ThumbPositionConverter,
    @ForHorizontalWsEdgeSlider
    private val thumbPositionConverterForHorizontalScrollBar:ThumbPositionConverter
) : InternalScrollBarAction {

    private var slider by sliderMs

    /**
     * When a scroll bar is drag, shift the worksheet slider either by some number of columns or rows. That number is computed from the position of the thumb.
     */
    override fun drag(data: ScrollBarActionType.Drag) {
        if(!data.data.thumbReachRailEnd) {
            when (data.data.scrollBarType) {
                ScrollBarType.Vertical -> {
                    val edgeRowRange = slider.edgeSliderRowRange
                    val newEdgeRow =
                        thumbPositionConverterForVerticalScrollBar.convertThumbPositionToIndex(edgeRowRange)

                    // move the top row to this
                    val shiftCount = newEdgeRow - slider.topLeftCell.rowIndex

                    if (shiftCount != 0) {
                        val newSlider = slider.shiftDown(shiftCount)
                        wsGetter.get()?.updateSliderAndRefreshDependentStates(newSlider)
                        slider = newSlider
                    }
                }

                ScrollBarType.Horizontal -> {
                    val edgeColRange = slider.edgeSliderColRange
                    val newEdgeCol =
                        thumbPositionConverterForHorizontalScrollBar.convertThumbPositionToIndex(edgeColRange)

                    // move the left col to this
                    val shiftCount = newEdgeCol - slider.topLeftCell.colIndex

                    if (shiftCount != 0) {
                        val newSlider = slider.shiftRight(shiftCount)
                        wsGetter.get()?.updateSliderAndRefreshDependentStates(newSlider)
                        slider = newSlider
                    }
                }
            }
        }
    }
}