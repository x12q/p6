package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar

object ScrollBarConstants {
    /**
     * A slider's maximum length percentage. Percentage of the rail
     */
    const val maxLength = 0.5f

    /**
     * A slider's minimum length percentage. Percentage of the rail
     */
    const val minLength = 0.1f

    const val reductionRate = 0.5f

    const val startingThumbPositionRatio = 0f

    /**
     * How much to move the thumb of a scrollbar back when it reaches the rail end. Must be between [0,1]
     */
    const val moveBackRatio = 0.65f

    /**
     * how much (by %) to move the thumb when user click on the edge slider rail
     * 0.1 = 10%
     */
    const val moveThumbByClickRatio = 0.1f
}