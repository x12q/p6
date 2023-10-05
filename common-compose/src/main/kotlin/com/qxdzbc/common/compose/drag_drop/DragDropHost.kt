package com.qxdzbc.common.compose.drag_drop

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import com.qxdzbc.common.compose.LayoutCoorsUtils.toP6Layout
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox

/**
 * Handle drag and drop action.
 * [content] can contain [Drag] and/or [Drop]
 * all the drags must have their id stored in [dragIds], all the drops in [dropIds].
 * id can be anything.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DragDropHost(
    internalStateMs: Ms<DragAndDropHostInternalState> = rms(DragAndDropHostInternalStateImp()),
    dragIds:Set<Any>,
    dropIds:Set<Any>,
    content: @Composable (
        internalStateMs: Ms<DragAndDropHostInternalState>
    ) -> Unit,
) {

    val state by internalStateMs
    if(dragIds!=state.acceptableDragIds){
        internalStateMs.value = state.setAcceptableDragIds(dragIds)
    }
    if(dropIds!=state.acceptableDropIds){
        internalStateMs.value = state.setAcceptableDropIds(dropIds)
    }

    Surface(
        color = Color.Black,
        modifier = Modifier
            .onPointerEvent(PointerEventType.Move) {
                if (state.isDragging) {
                    it.changes.getOrNull(0)?.position?.also { mousePos ->
                        state.hostCoorWrapper?.let { cp ->
                            val mousePosInWindow = cp.localToWindow(mousePos)
                            internalStateMs.value = state.setMousePositionWindow(mousePosInWindow)
                        }
                    }
                }
            }
    ) {
        MBox(
            modifier = Modifier.onGloballyPositioned {
                internalStateMs.value = state.setHostLayoutCoorWrapper(it.toP6Layout())
            }
        ) {
            content(internalStateMs)
        }
    }
}




