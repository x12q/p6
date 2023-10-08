package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state

interface ThumbPositionConverter{
    /**
     * Convert thumb position between [0,1] to an int within [indexRange]
     */
    fun convertThumbPositionToIndex(indexRange:IntRange):Int
}