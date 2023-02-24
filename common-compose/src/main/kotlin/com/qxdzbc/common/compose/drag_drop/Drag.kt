package com.qxdzbc.common.compose.drag_drop

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox


/**
 * TODO use local provider to pass the internal state implicitly to this function
 * There maybe multiple draggable in a host, each drag need an id to identify which one is being dragged.
 * [onDragStart]: callback triggered when dragging action starts
 * [onDragStop]: callback triggered when dragging action stops
 * [onDrop]: callback triggered when a Drag is dropped on on a valid Drop. If it is not dropped on a valid Drop, this callback won't be triggered.
 */
@Composable
fun Drag(
    internalStateMs: Ms<DragAndDropHostInternalState>,
    identifier: Any,
    onDragStart: () -> Unit = {},
    onDragStop: () -> Unit = {},
    onDrop: (dragId: Any, dropId: Any) -> Unit = { _, _ -> },
    content: @Composable () -> Unit,
) {
    val state by internalStateMs
    var offsetX by rms(0f)
    var offsetY by rms(0f)
    MBox(
        modifier = Modifier
            .offset {
                if (state.isDragging && identifier == state.currentDrag) {
                    IntOffset(offsetX.toInt(), offsetY.toInt())
                } else {
                    Offset.Zero.toIntOffset()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        internalStateMs.value = state
                            .setIsDragging(true)
                            .setCurrentDrag(identifier)
                        onDragStart()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    },
                    onDragEnd = {
                        val dropId: Any? = state.detectDrop(identifier)
                        dropId?.also {
                            onDrop(identifier, it)
                        }
                        internalStateMs.value = state.resetToNonDragState()
                        offsetX = 0f
                        offsetY = 0f
                        onDragStop()
                    }
                )
            }
            .onGloballyPositioned {
                internalStateMs.value = state.addDragLayoutCoorWrapper(identifier, it.wrap())
            }
    ) {
        content()
    }
}
