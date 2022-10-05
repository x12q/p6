package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.OffsetUtils.toIntOffset
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.BorderBox

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ThumbView(
    state:ThumbState,
    action: ThumbAction,
) {
    val size: DpSize = remember{DpSize(10.dp,10.dp)}
    var thumbLayout:LayoutCoordinates? by rms(null)
    MBox(
        modifier = Modifier
            .size(size)
            .onGloballyPositioned {
                thumbLayout = it
            }
            .background(P6R.color.cursorColor)
            .pointerHoverIcon(P6R.mouse.icon.thumbMouseIcon)
            .onPointerEvent(PointerEventType.Press){pointerEvn->
                thumbLayout?.also {
                    val mousePosInWindow = it.localToWindow(pointerEvn.changes[0].position)
                    action.startDrag(state.cursorId,mousePosInWindow)
                }
            }
            .onPointerEvent(PointerEventType.Move){ptEvn->
                thumbLayout?.also {
                    val mousePosInWindow = it.localToWindow(ptEvn.changes[0].position)
                    action.drag(state.cursorId,mousePosInWindow)
                }
            }
            .onPointerEvent(PointerEventType.Release){ptEvn->
                thumbLayout?.also {
                    val mousePosInWindow = it.localToWindow(ptEvn.changes[0].position)
                    action.endDrag(state.cursorId,mousePosInWindow)
                }
            }
    )
}
