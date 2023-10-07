package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar

object ScrollBarConstants {
    /**
     * A slider's maximum length percentage. Percentage of the rail.
     *
     * 0.5 = 50%
     */
    const val maxLength = 0.5f

    /**
     * A slider's minimum length percentage. Percentage of the rail.
     *
     * 0.1 = 10%
     */
    const val minLength = 0.1f

    /**
     * How much to reduce the length of a slider when it reaches the rail end. Must be between [0,1].
     *
     * 0.5 = 50%
     */
    const val reductionRate = 0.5f

    /**
     * The starting position of a slider's thumb. Percentage of the rail.
     *
     * 0 = 0%
     */
    const val startingThumbPositionRatio = 0f

    /**
     * How much to move the thumb of a scrollbar back when it reaches the rail end. Must be between [0,1].
     *
     * 0.65 = 65%
     */
    const val moveBackRatio = 0.65f

    /**
     * how much (by %) to move the thumb when user click on the edge slider rail
     *
     * 0.1 = 10%
     */
    const val moveThumbByClickRatio = 0.1f
}