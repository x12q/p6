package com.qxdzbc.p6.ui.worksheet.slider.edge_slider

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6LayoutCoor
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.HSpacer
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderRail
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderThumb
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderStateImp


/**
 * Edge slider is a slider at the edge of a worksheet.
 * User can drag on this slider to scroll the worksheet vertically or horizontally.
 * An edge slider consist of a [SliderRail] and a [SliderThumb].
 * The rail takes up the entire length of the slider, and the thumb resides on top of the rail, and can move back and fort.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerticalEdgeSlider(
    state: EdgeSliderState,
    railModifier: Modifier = Modifier,
    thumbModifier: Modifier = Modifier,
    onDrag: (positionRatio: Float) -> Unit,
    onClickOnRail: (clickPositionRatio: Float) -> Unit,
    allowComputationAtBot: () -> Boolean = { true },
) {
    val density = LocalDensity.current

    var isPressed by rms(false)
    var isDragged by rms(false)

    SliderRail(
        modifier = railModifier
            .onGloballyPositioned {
                state.railLayoutCoor = it.toP6LayoutCoor(state.thumbLayoutCoor?.refreshVar)
            }
            .onPointerEvent(PointerEventType.Press) {
                isPressed = true
            }
            .onPointerEvent(PointerEventType.Move) {
                if (isPressed) {
                    isDragged = true
                }
            }
            .onPointerEvent(PointerEventType.Release) { pte ->
                if (!isDragged) {
                    pte.changes.firstOrNull()?.position?.also { clickPointOffset ->
                        state.computePositionRatioOnFullRail(clickPointOffset.y)?.let { ratio ->
                            state.performMoveThumbWhenClickOnRail(ratio)
                            onClickOnRail(ratio)
                        }
                    }
                }
                isPressed = false
                isDragged = false
            }
    ) {
        SliderThumb(
            length = state.computeThumbLength(density),
            offset = DpOffset(x = 0.dp, y = with(density) { state.thumbPositionInPx.toDp() }),
            modifier = thumbModifier
                .onGloballyPositioned {
                    state.thumbLayoutCoor = it.toP6LayoutCoor(state.thumbLayoutCoor?.refreshVar)
                }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        state.recomputeStateWhenThumbIsDragged(delta, allowComputationAtBot())
                        onDrag(state.thumbPositionRatio)
                    }
                )
        )
    }
}

@Preview
@Composable
fun Preview_VerticalEdgeSlider() {

    val sliderState: Ms<GridSlider> = rms(GridSliderImp.forPreview())
    val state = remember {
        EdgeSliderStateImp()
    }

    var dragRatio: Float? by rms(null)
    var clickRatio: Float? by rms(null)

    Row {
        VerticalEdgeSlider(
            state = state,
            onDrag = {
                dragRatio = it
            },
            onClickOnRail = {
                clickRatio = it
            }
        )
        HSpacer(50.dp)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Click ratio: ${clickRatio}")

            Text("Drag ratio: ${dragRatio}")

            Text("Thumb position ratio: ${state.thumbPositionRatio}")

            Text("${sliderState}")
        }

    }

}

fun main() {
    testApp {
        Preview_VerticalEdgeSlider()
    }
}