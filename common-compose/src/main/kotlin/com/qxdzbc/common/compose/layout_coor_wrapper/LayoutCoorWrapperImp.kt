package com.qxdzbc.common.compose.layout_coor_wrapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.DpSize
import com.qxdzbc.common.compose.LayoutCoorsUtils.ifAttached
import com.qxdzbc.common.compose.SizeUtils.toDpSize

data class LayoutCoorWrapperImp(
    override val layout: LayoutCoordinates,
    override val refreshVar: Boolean = true
) : LayoutCoorWrapper {

    override val sizeOrZero: DpSize get() = size?: DpSize.Zero

    override val size: DpSize?
        get() = layout.ifAttached {lc->
            lc.size.toDpSize()
        }

    override val boundInWindowOrZero: Rect get() = boundInWindow ?: Rect(Offset.Zero, Size.Zero)

    override val boundInWindow: Rect?
        get() {
            if (layout.isAttached) {
                return layout.boundsInWindow()
            } else {
                return null
            }
        }
    override val posInWindowOrZero: Offset get() = posInWindow ?: Offset(0F, 0F)
    override val posInWindow: Offset?
        get() {
            if (layout.isAttached) {
                return layout.positionInWindow()
            } else {
                return null
            }
        }

    override fun localToWindow(local: Offset): Offset {
        return this.layout.localToWindow(local)
    }

    override fun windowToLocal(window: Offset): Offset {
        return this.layout.windowToLocal(window)
    }

    override val isAttached: Boolean
        get() {
            return layout.isAttached
        }

    override fun ifAttached(f: (lc: LayoutCoorWrapper) -> Unit) {
        if (this.layout.isAttached) {
            f(this)
        }
    }

    @Composable
    override fun ifAttachedComposable(f: @Composable (lc: LayoutCoorWrapper) -> Unit) {
        if (this.layout.isAttached) {
            f(this)
        }
    }

    override fun forceRefresh(i: Boolean): LayoutCoorWrapper {
        return this.copy(refreshVar = i)
    }

    override fun forceRefresh(): LayoutCoorWrapper {
        return this.copy(refreshVar = !refreshVar)
    }
}
