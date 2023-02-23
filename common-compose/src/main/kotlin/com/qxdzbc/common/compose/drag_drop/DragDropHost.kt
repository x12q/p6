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
import com.qxdzbc.common.compose.LayoutCoorsUtils.wrap
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.view.MBox

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DragDropHost(
    internalStateMs: Ms<DragAndDropHostState>,
    content: @Composable (
        internalStateMs: Ms<DragAndDropHostState>
    ) -> Unit,
) {
    val state by internalStateMs
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
                internalStateMs.value = state.setHostLayoutCoorWrapper(it.wrap())
            }
        ) {
            content(internalStateMs)
        }
    }
}




