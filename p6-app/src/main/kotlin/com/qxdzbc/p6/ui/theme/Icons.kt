package com.qxdzbc.p6.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.input.pointer.PointerIcon
import java.awt.Cursor

object Icons {
    val downResize = PointerIcon(Cursor(Cursor.S_RESIZE_CURSOR))
    val leftResize = PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR))
    val rightResize = PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))
    val upResize = PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))
    val thumbMouseIcon = PointerIcon(Cursor(Cursor.CROSSHAIR_CURSOR))

    val local = staticCompositionLocalOf(
        defaultFactory = {
            Icons
        }
    )
}