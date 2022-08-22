package com.qxdzbc.p6.ui.common.view.resizable_box

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.R
import com.qxdzbc.p6.ui.common.compose.LayoutCoorsUtils.ifAttached
import com.qxdzbc.p6.ui.common.compose.Ms

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
        override val icon = R.mouse.leftResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return -(currentMousePos.x - anchorMousePos.x)
        }
    }

    object right : ResizeOperations {
        override val icon = R.mouse.rightResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return currentMousePos.x - anchorMousePos.x
        }

    }

    object top : ResizeOperations {
        override val icon = R.mouse.upResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return -(currentMousePos.y - anchorMousePos.y)
        }

    }

    object bot : ResizeOperations {
        override val icon = R.mouse.downResize
        override fun difFunction(anchorMousePos: Offset, currentMousePos: Offset): Float {
            return currentMousePos.y - anchorMousePos.y
        }
    }
}
