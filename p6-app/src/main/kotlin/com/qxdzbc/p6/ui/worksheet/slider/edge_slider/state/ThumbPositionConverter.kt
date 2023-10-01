package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

interface ThumbPositionConverter{
    /**
     * Convert [thumbPositionRatio] to an int within [indexRange]
     */
    fun convertThumbPositionToIndex(indexRange:IntRange):Int
}