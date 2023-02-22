package com.qxdzbc.common.compose.drag_drop

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.view.MBox
import kotlin.math.roundToInt

@Composable
fun Drag(
    internalStateMs: Ms<DragAndDropHostState>,
    identifier: () -> Any,
    onClick: () -> Unit = {},
    onDrop: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Drag(
        internalStateMs = internalStateMs,
        identifier = identifier(),
        onClick = onClick,
        onDrop = onDrop,
        content = content
    )
}

/**
 * TODO use local provider to pass the internal state implicitly to this function
 * There maybe multiple draggable in a host, each drag need an id to identify which one is being dragged.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Drag(
    internalStateMs: Ms<DragAndDropHostState>,
    identifier: Any,
    onClick: () -> Unit = {},
    onDrop: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val state by internalStateMs
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    MBox(
        modifier = Modifier
            .offset {
                if (state.isClicked && identifier == state.currentDrag) {
                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                } else {
                    Offset.Zero.toIntOffset()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .onPointerEvent(PointerEventType.Press) {
                internalStateMs.value = state
                    .setIsClicked(true)
                    .setCurrentDrag(identifier)
                    .let { st ->
                        // store the original position of the current drag to the state
                        val posInWindow = state.dragMap[identifier]?.posInWindow
                        posInWindow?.let { st.setCurrentDragOriginalPositionInWindow(it) } ?: st
                    }
                onClick()
            }.onPointerEvent(PointerEventType.Release) {
                internalStateMs.value = state.resetToNonDragState()
                offsetX = 0f
                offsetY = 0f
                onDrop()
            }.onGloballyPositioned {
                internalStateMs.value = state.addDragLayoutCoorWrapper(identifier, it.wrap())
            }) {
        content()
    }
}
