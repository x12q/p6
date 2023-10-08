package com.qxdzbc.common.compose.layout_coor_wrapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import com.qxdzbc.common.compose.LayoutCoorsUtils.ifAttached
import com.qxdzbc.common.compose.SizeUtils.toDpSize

data class P6LayoutImp(
    override val layout: LayoutCoordinates,
    override val refreshVar: Boolean = true
) : P6Layout {

    override fun setLayout(i: LayoutCoordinates): P6Layout {
        return this.copy(
            layout = i,
            refreshVar = !refreshVar
        )
    }

    override val pixelSizeOrZero: IntSize get() = pixelSize?: IntSize.Zero

    override val pixelSize: IntSize?
        get() = layout.ifAttached {lc->
            lc.size
        }

    override fun dbSize(density: Density): DpSize? {
        return layout.ifAttached {lc->
            lc.size.toDpSize(density)
        }
    }

    override fun dbSizeOrZero(density: Density): DpSize {
        return dbSize(density)?: DpSize.Zero
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
    override val boundInParent: Rect?
        get() {
            if (layout.isAttached) {
                return layout.boundsInParent()
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

    override fun ifAttached(f: (lc: P6Layout) -> Unit) {
        if (this.layout.isAttached) {
            f(this)
        }
    }

    @Composable
    override fun ifAttachedComposable(f: @Composable (lc: P6Layout) -> Unit) {
        if (this.layout.isAttached) {
            f(this)
        }
    }

    override fun forceRefresh(i: Boolean): P6Layout {
        return this.copy(refreshVar = i)
    }

    override fun forceRefresh(): P6Layout {
        return this.copy(refreshVar = !refreshVar)
    }
}
