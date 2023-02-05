package com.qxdzbc.p6.ui.drag_drop.collider

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset

interface Collider2D {
    fun isCollide(another:Collider2D):Boolean
    val position:IntOffset
    val size:Size
}
