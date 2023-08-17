package com.qxdzbc.p6.ui.worksheet.slider.edge_slider

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.HSpacer
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderRail
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderThumb
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderStateImp


/**
 * Edge slider is a slider at the end of a worksheet. User can drag on this slider to scroll the worksheet vertically or horizontally.
 *
 * Source of truth: grid slider.
 *
 * Edge slider size and sliding speed reflect that of grid slider
 * Edge slider can change grid slider, hence itself.
 *
 */
@Composable
fun VerticalEdgeSlider(
    state: VerticalEdgeSliderState,
    onDrag: (positionRatio:Float) -> Unit = {},
    onClickOnRail: (clickPositionRatio: Float) -> Unit = {},
) {
    val density = LocalDensity.current

    SliderRail(
        modifier = Modifier.onGloballyPositioned {
            state.railLayoutCoor = it.wrap(state.thumbLayoutCoor?.refreshVar)
        }) {
        val railLength = with(density){state.railLengthPx?.toDp()} ?: 0.dp
        SliderThumb(
            length = state.computeRelativeThumbLength(railLength),
            offset = state.thumbPosition,
            modifier = Modifier
                .onGloballyPositioned {
                    state.thumbLayoutCoor = it.wrap(state.thumbLayoutCoor?.refreshVar)
                }
                .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    state.recomputeStateWhenThumbIsDragged(density,delta)
                }
            )
        )
    }
}

@Preview
@Composable
fun Preview_VerticalEdgeSlider() {

    val sliderState:Ms<GridSlider> = rms(GridSliderImp.forPreview())
    val state by rms(VerticalEdgeSliderStateImp(
        sliderStateMs = sliderState
    ))

    Row {
        VerticalEdgeSlider(
            state = state,
        )
        HSpacer(50.dp)
        Column {
            Text("${state.slideRatio}")
            HSpacer(10.dp)
            Text("${sliderState}")
        }

    }

}

fun main() {
    testApp {
        Preview_VerticalEdgeSlider()
    }
}