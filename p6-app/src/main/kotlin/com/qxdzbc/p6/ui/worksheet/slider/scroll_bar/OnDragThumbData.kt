package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType

/**
 * Position data for on drag-thumb event
 * [realPositionRatio] is the position ratio of the top of the thumb. It indicates the real position of the thumb. This number is between [0,1], but it never reaches 1
 * [virtualPositionRatio] is the virtual position of thumb. This one should be used to compute derivative positions such as worksheet scroll position. This is between [0,1].
 * [scaleRatio] denotes how big is the thumb compared to the whole slider. It is used to compute scroll speed.
 */
data class OnDragThumbData(
    val realPositionRatio: Float,
    val virtualPositionRatio:Float,
    private val scaleRatio:Float,
    val scrollBarType: ScrollBarType,
    val thumbReachRailEnd:Boolean,
)