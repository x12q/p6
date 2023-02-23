package com.qxdzbc.common.compose.layout_coor_wrapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.DpSize

/**
 * For testing only
 */
class DummyLayoutCoorWrapper(
    override val size: DpSize? = DpSize.Zero,
    override val boundInWindow: Rect? = Rect.Zero,
    override val posInWindow: Offset? = Offset.Zero,
    override val isAttached: Boolean = true,
) : LayoutCoorWrapper {

    override val boundInWindowOrZero: Rect get() = boundInWindow ?: Rect.Zero
    override val posInWindowOrZero: Offset get() = posInWindow ?: Offset.Zero
    override val sizeOrZero: DpSize get() = size ?: DpSize.Zero

    override val refreshVar: Boolean get() = throw UnsupportedOperationException()
    override val layout: LayoutCoordinates get() = throw UnsupportedOperationException()

    override fun localToWindow(local: Offset): Offset {
        throw UnsupportedOperationException()
    }

    override fun windowToLocal(window: Offset): Offset {
        throw UnsupportedOperationException()
    }

    override fun ifAttached(f: (lc: LayoutCoorWrapper) -> Unit) {
        throw UnsupportedOperationException()
    }
    @Composable
    override fun ifAttachedComposable(f:@Composable (lc: LayoutCoorWrapper) -> Unit) {
        throw UnsupportedOperationException()
    }

    override fun forceRefresh(i: Boolean): LayoutCoorWrapper {
        throw UnsupportedOperationException()
    }

    override fun forceRefresh(): LayoutCoorWrapper {
        throw UnsupportedOperationException()
    }
}
