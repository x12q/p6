package com.qxdzbc.p6.ui.worksheet.slider.edge_slider

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.worksheet.slider.LimitedSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderRail
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderThumb
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderStateImp


/**
 * Edge slider is a slider at the end of a worksheet. User can drag on this slider to scroll the worksheet vertically or horizontally
 */
@Composable
fun VerticalEdgeSlider(
    state: VerticalEdgeSliderState,
    onDrag: (positionRatio:Float) -> Unit = {},
    onClickOnRail: (clickPositionRatio: Float) -> Unit = {},
) {
    var railLayout: LayoutCoorWrapper? by rms(null)
    var thumbLayout: LayoutCoorWrapper? by rms(null)
    val density = LocalDensity.current

    SliderRail(
        modifier = Modifier.onGloballyPositioned {
            railLayout = it.wrap()
        }) {
        val railLength = railLayout?.dbSize(density)?.height ?: 0.dp
        SliderThumb(
            length = state.thumbLength(railLength),
            offset = state.thumbPosition,
            modifier = Modifier
                .onGloballyPositioned {
                    thumbLayout = it.wrap()
                }
                .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    state.setThumbOffsetWhenDrag(density, delta)
                    // detect if slider reach the bottom

                    val thumbYBottom = thumbLayout?.boundInWindow?.bottom
                    val railYBottom = railLayout?.boundInWindow?.bottom

                    val thumbReachRailBottom = thumbYBottom!=null && railYBottom!=null && thumbYBottom==railYBottom

                    if(thumbReachRailBottom){
                        state.recomputeStateWhenThumbReachRailBottom(railLength)
                    }else{
                        val thumbYTop = thumbLayout?.boundInWindow?.top
                        val railYTop = railLayout?.boundInWindow?.top

                        val thumbReachRailTop = railYTop!=null && thumbYTop!=null && railYTop==thumbYTop
                        if(thumbReachRailTop){
                            state.recomputeStateWhenThumbReachRailTop()
                        }
                    }
                }
            )
        )
    }
}

@Preview
@Composable
fun Preview_VerticalEdgeSlider() {

    val state by rms(VerticalEdgeSliderStateImp(
        sliderStateSt = ms(LimitedSliderImp.forPreview())
    ))

    VerticalEdgeSlider(
        state = state,
    )
}

fun main() {
    testApp {
        Preview_VerticalEdgeSlider()
    }
}