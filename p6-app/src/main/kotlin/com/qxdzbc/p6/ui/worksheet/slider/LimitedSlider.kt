package com.qxdzbc.p6.ui.worksheet.slider

/**
 * A slider that can only shift within bounds defined by [colLimit] and [rowLimit]
 */
interface LimitedSlider : GridSlider {
    val colLimit: IntRange
    val rowLimit: IntRange
}