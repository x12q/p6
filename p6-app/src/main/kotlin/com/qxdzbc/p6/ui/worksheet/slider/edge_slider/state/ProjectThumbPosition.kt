package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.state

interface ProjectThumbPosition{
    /**
     * Convert [thumbPositionRatio] to an int within [indexRange]
     */
    fun projectThumbPositionToIndex(indexRange:IntRange):Int
}