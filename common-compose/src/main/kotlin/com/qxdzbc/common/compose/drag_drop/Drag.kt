package com.qxdzbc.common.compose.drag_drop

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.view.MBox

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
    MBox(
        modifier = Modifier
            .let { mod ->
                if (state.isClicked && identifier == state.currentDrag) {

                    val localMousePos = state.mousePosition?.let {
                        state.hostCoorWrapper?.windowToLocal(it)
                    }
                    val originalPos = state.currentDragOriginalPosition
                    // offset = offset from original position
                    val newOffset = if(localMousePos!=null && originalPos!=null){
                        localMousePos.minus(originalPos)
                    }else{
                        null
                    }
                    val newMod = newOffset?.let { mod.offset { it.toIntOffset() } }?: mod
                    newMod
                } else {
                    mod
                }
            }
            .onPointerEvent(PointerEventType.Press) {
                internalStateMs.value = state
                    .setIsClicked(true)
                    .setCurrentDrag(identifier)
                     .let {st->
                         val posInWindow = state.dragMap[identifier]?.posInWindow
                         posInWindow?.let { st.setCurrentDragOriginalPosition(it) } ?: st
                     }
                onClick()
            }.onPointerEvent(PointerEventType.Release) {
                internalStateMs.value = state.resetToNonDragState()
                onDrop()
            }.onGloballyPositioned {
                internalStateMs.value = state.setDragLayoutCoorWrapper(identifier, it.wrap())
            }) {
        content()
    }
}
