package com.qxdzbc.p6.ui.common.view.resizable_box

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.LayoutCoorsUtils.ifAttached
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * Describe resize operation (which edge the mouse is on) for a mouse cursor on a resizable box
 */
sealed interface ResizeOperations {
    val icon: PointerIcon
    fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float
    fun onPress(pte:PointerEvent, lc:LayoutCoordinates?,anchorMousePosMs: Ms<Offset?>){
        lc.ifAttached {l->
            pte.changes.firstOrNull()?.position?.also {
                anchorMousePosMs.value = l.localToWindow(it)
            }
        }
    }
    fun moveFunction(
        anchorSize: Dp,
        minSize: Dp,
        d: Ms<Dp>,
        anchorMousePos: Offset?,
        currentMousePos: Offset?,
    ) {
        val a = anchorMousePos
        val c = currentMousePos
        if (a != null && c != null) {
            val newSize = anchorSize + difFunction(a, c).dp
            d.value = maxOf(newSize, minSize)
        }
    }

    companion object {
        fun getOperationFor(resizeStyle: ResizeStyle): ResizeOperations {
            return when (resizeStyle) {
                ResizeStyle.__BOT -> bot
                ResizeStyle.__TOP -> top
                ResizeStyle.__LEFT -> left
                ResizeStyle.__RIGHT -> right
            }
        }
    }

    object left : ResizeOperations {
        override val icon = P6Theme.icons.leftResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return -(currentMousePos.x - anchorMousePos.x)
        }
    }

    object right : ResizeOperations {
        override val icon = P6Theme.icons.rightResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return currentMousePos.x - anchorMousePos.x
        }

    }

    object top : ResizeOperations {
        override val icon = P6Theme.icons.upResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return -(currentMousePos.y - anchorMousePos.y)
        }

    }

    object bot : ResizeOperations {
        override val icon = P6Theme.icons.downResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return currentMousePos.y - anchorMousePos.y
        }
    }
}
