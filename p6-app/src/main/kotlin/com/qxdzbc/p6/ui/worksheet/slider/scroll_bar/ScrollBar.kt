package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6Layout
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.HSpacer
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.ui.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.component.Rail
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.component.Thumb
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.HorizontalScrollBarState
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.VerticalScrollBarState

/**
 * Scroll bar is a slider at the edge of a worksheet.
 * User can drag on this slider to scroll the worksheet vertically or horizontally.
 * A scroll bar consist of a [Rail] and a [Thumb].
 * - [Rail] takes up the entire length of the slider,
 * - [Thumb] resides on top of the rail, and can move back and fort.
 * A [ScrollBar] give its consumer the following information:
 * - in [onDrag], callers get access to position data of thumb on the rail. This data can be translated into position at the caller's end.
 * - in [onClickOnRail], callers get access to the position ratio [0,1] of the click position on the rail.
 * - a [allowComputationAtEnd] TODO ??
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScrollBar(
    state: ScrollBarState,
    railModifier: Modifier = Modifier,
    thumbModifier: Modifier = Modifier,
    onDrag: (positionRatio: OnDragThumbData) -> Unit,
    onClickOnRail: (clickPositionRatio: Float) -> Unit,
    allowComputationAtEnd: Boolean,
) {
    val density = LocalDensity.current

    var isPressed by rms(false)
    var isDragged by rms(false)

    Rail(
        type = state.type,
        modifier = railModifier
            .onGloballyPositioned {
                state.railLayoutCoor = it.toP6Layout(state.thumbLayoutCoor?.refreshVar)
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

                        val clickPosition = when (state) {
                            is HorizontalScrollBarState -> clickPointOffset.x
                            is VerticalScrollBarState -> clickPointOffset.y
                            else -> {
                                TODO()
                            }
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
                is HorizontalScrollBarState -> Orientation.Horizontal
                is VerticalScrollBarState -> Orientation.Vertical
                else -> {
                    TODO()
                }
            }
        }

        Thumb(
            type = state.type,
            length = state.computeThumbLength(density),
            offset = state.computeThumbOffset(density),
            modifier = thumbModifier
                .onGloballyPositioned { layout ->
                    state.thumbLayoutCoor = layout.toP6Layout(state.thumbLayoutCoor)
                }
                .draggable(
                    orientation = dragOrientation,
                    state = rememberDraggableState { delta ->
                        state.recomputeStateWhenThumbIsDragged(delta, allowComputationAtEnd)
                        onDrag(state.onDragData)
                    }
                )
        )
    }
}


@Preview
@Composable
fun Preview_VerticalScrollBar() {

    val state = remember {
        VerticalScrollBarState()
    }

    var dragRatio: OnDragThumbData? by rms(null)
    var clickRatio: Float? by rms(null)

    Row {
        ScrollBar(
            state = state,
            onDrag = {
                dragRatio = it
            },
            onClickOnRail = {
                clickRatio = it
            },
            allowComputationAtEnd = false,
        )
        HSpacer(50.dp)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Click ratio: ${clickRatio}")

            Text("Drag ratio: ${dragRatio}")

            Text("Thumb position ratio: ${state.thumbPositionRatio}")

            Text("Thumb scroll ratio: ${state.effectiveThumbPositionRatio}")


        }

    }

}



@Preview
@Composable
fun Preview_HorizontalBar() {

    val sliderState: Ms<GridSlider> = rms(GridSliderImp.forPreview())
    val state = remember {
        HorizontalScrollBarState()
    }

    var dragRatio: OnDragThumbData? by rms(null)
    var clickRatio: Float? by rms(null)

    Column {
        ScrollBar(
            state = state,
            onDrag = {
                dragRatio = it
            },
            onClickOnRail = {
                clickRatio = it
            },
            allowComputationAtEnd = true,
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
        Preview_VerticalScrollBar()
    }
}