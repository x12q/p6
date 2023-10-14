package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForHorizontalScrollBar
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalScrollBar
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.*
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import javax.inject.Inject

/**
 * To handle user drag action on thumb
 */
@WsScope
class DragThumb @Inject constructor(
    private val wsGetter: WorksheetStateGetter,
    private val sliderMs: Ms<GridSlider>,
    @ForVerticalScrollBar
    private val thumbPositionConverterForVerticalScrollBar: ThumbPositionConverter,
    @ForHorizontalScrollBar
    private val thumbPositionConverterForHorizontalScrollBar: ThumbPositionConverter,
) {

    private var slider by sliderMs

    /**
     * When a scroll bar is drag, shift the worksheet slider either by some number of columns or rows. That number is computed from the position of the thumb.
     */
    fun run(data: ScrollBarActionData.Drag) {
        if (!data.state.thumbReachRailEnd) {
            when (data.state.type) {
                ScrollBarType.Vertical -> {
                    val edgeRowRange = slider.scrollBarRowRange
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
                    val edgeColRange = slider.scrollBarColRange
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