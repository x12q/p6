package com.qxdzbc.common.compose.drag_drop

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toOffset
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.view.MBox

/**
 * TODO use local provider to pass the internal state implicitly to this function
 * There maybe multiple draggable in a host, each drag need an id to identify which one is being dragged.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Drag(
    internalStateMs: Ms<DragAndDropHostState>,
    identifier: () -> Any,
    onClick: () -> Unit = {},
    onDrop: () -> Unit = {},
    content: @Composable () -> Unit,
) {

    val state = internalStateMs.value

    MBox(
        modifier = Modifier
            .let { mod ->
                if (state.isClicked && identifier() == state.currentDrag) {
                    state.mousePosition?.let { state.hostCoorWrapper?.windowToLocal(it.toOffset()) }
                        ?.let { mod.offset { it.toIntOffset() } }
                        ?: mod
                } else {
                    mod
                }
            }
            .onPointerEvent(PointerEventType.Press) {
                internalStateMs.value = state
                    .setIsClicked(true)
                    .setCurrentDrag(identifier())
                onClick()
            }.onPointerEvent(PointerEventType.Release) {
                internalStateMs.value = state.resetToNonDragState()
                onDrop()
            }.onGloballyPositioned {
                internalStateMs.value = state.setDragLayoutCoorWrapper(identifier(), it.wrap())
            }) {
        content()
    }
}
