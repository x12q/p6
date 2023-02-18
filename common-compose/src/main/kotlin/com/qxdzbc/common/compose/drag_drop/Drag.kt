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
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Drag(
    internalStateMs: Ms<DragAndDropHostState>,
    onClick: () -> Unit,
    onDrop: () -> Unit,
    content: @Composable () -> Unit,
) {
    val state = internalStateMs.value
    val pos = state.mousePosition
    MBox(
        modifier = Modifier
            .let { mod ->
                pos?.let{state.dragHostCoorWrapper?.windowToLocal(pos.toOffset())}?.let {
                    mod.offset { it.toIntOffset() }
                } ?: mod
            }
            .onPointerEvent(PointerEventType.Press) {
                internalStateMs.value = state.setIsClicked(true)
                onClick()
            }.onPointerEvent(PointerEventType.Release) {
                internalStateMs.value = state.setIsClicked(false).setMousePosition(null)
                onDrop()
            }.onGloballyPositioned {
                internalStateMs.value = state.setDragObjCoorWrapper(it.wrap())
            }){
        content()
    }
}
