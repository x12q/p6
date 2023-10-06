package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.EdgeSliderActionType
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForHorizontalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.di.qualifiers.ForVerticalWsEdgeSlider
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ThumbPositionConverter
import com.qxdzbc.p6.ui.worksheet.state.WorksheetStateGetter
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class InternalEdgeSliderActionImp @Inject constructor(
    private val wsStateGetter: WorksheetStateGetter,
    private val sliderMs: Ms<GridSlider>,
    @ForVerticalWsEdgeSlider
    private val thumbPositionConverterForVerticalScrollBar: ThumbPositionConverter,
    @ForHorizontalWsEdgeSlider
    private val thumbPositionConverterForHorizontalScrollBar:ThumbPositionConverter
) : InternalEdgeSliderAction {

    private var slider by sliderMs

    override fun drag(data: EdgeSliderActionType.Drag) {
        when(data.data.scrollBarType){
            ScrollBarType.Vertical -> {
                val edgeRowRange = slider.edgeSliderRowRange
                val newEdgeRow = thumbPositionConverterForVerticalScrollBar.convertThumbPositionToIndex(edgeRowRange)

                // move the top row to this
                val shiftCount = newEdgeRow - slider.topLeftCell.rowIndex

                if(shiftCount!=0){
                    val newSlider = slider.shiftDown(shiftCount)
                    wsStateGetter.get()?.setSliderAndRefreshDependentStates(newSlider)
                    slider = newSlider
                }
            }
            ScrollBarType.Horizontal -> {
//                println(data.data)
                val edgeColRange = slider.edgeSliderColRange
                val newEdgeCol = thumbPositionConverterForHorizontalScrollBar.convertThumbPositionToIndex(edgeColRange)
//                println("newEdgeCol ${newEdgeCol}")

                // move the left col to this
                val shiftCount = newEdgeCol - slider.topLeftCell.colIndex
//                println("shiftCount $shiftCount")

                if(shiftCount!=0){
                    println(shiftCount)
                    val newSlider = slider.shiftRight(shiftCount)
                    wsStateGetter.get()?.setSliderAndRefreshDependentStates(newSlider)
                    slider = newSlider
                }
            }
        }
    }
}