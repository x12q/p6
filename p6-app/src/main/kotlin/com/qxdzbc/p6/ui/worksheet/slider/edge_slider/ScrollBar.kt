package com.qxdzbc.p6.ui.worksheet.slider.edge_slider

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6LayoutCoor
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderRail
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component.SliderThumb
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.EdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.HorizontalEdgeSliderState
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state.VerticalEdgeSliderState

/**
 * Edge slider is a slider at the edge of a worksheet.
 * User can drag on this slider to scroll the worksheet vertically or horizontally.
 * An edge slider consist of a [SliderRail] and a [SliderThumb].
 * - [SliderRail] takes up the entire length of the slider,
 * - [SliderThumb] resides on top of the rail, and can move back and fort.
 * A [ScrollBar] give its consumer the following information:
 * - in [onDrag], callers get access to position data of thumb on the rail. This data can be translated into position at the caller's end.
 * - in [onClickOnRail], callers get access to the position ratio [0,1] of the click position on the rail.
 * - a [allowComputationAtEnd] TODO ??
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScrollBar(
    state: EdgeSliderState,
    railModifier: Modifier = Modifier,
    thumbModifier: Modifier = Modifier,
    onDrag: (positionRatio: OnDragThumbData) -> Unit,
    onClickOnRail: (clickPositionRatio: Float) -> Unit,
    allowComputationAtEnd: () -> Boolean = { true },
) {
    val density = LocalDensity.current

    var isPressed by rms(false)
    var isDragged by rms(false)

    SliderRail(
        type = state.type,
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

                        val clickPosition = when(state){
                            is HorizontalEdgeSliderState -> clickPointOffset.x
                            is VerticalEdgeSliderState -> clickPointOffset.y
                        }

                        state.computePositionRatioOnFullRail(clickPosition)?.let { ratio ->
                            state.performMoveThumbWhenClickOnRail(ratio)
                            onClickOnRail(ratio)
                        }
                    }
                }
                isPressed = false
                isDragged = false
            }
    ) {
        val dragOrientation = remember {
            when (state) {
                is HorizontalEdgeSliderState -> Orientation.Horizontal
                is VerticalEdgeSliderState -> Orientation.Vertical
            }
        }

        SliderThumb(
            type = state.type,
            length = state.computeThumbLength(density),
            offset = state.computeThumbOffset(density),
            modifier = thumbModifier
                .onGloballyPositioned {
                    state.thumbLayoutCoor = it.toP6LayoutCoor(state.thumbLayoutCoor?.refreshVar)
                }
                .draggable(
                    orientation = dragOrientation,
                    state = rememberDraggableState { delta ->
                        state.recomputeStateWhenThumbIsDragged(delta, allowComputationAtEnd())
                        onDrag(state.onDragData)
                    }
                )
        )
    }
}
