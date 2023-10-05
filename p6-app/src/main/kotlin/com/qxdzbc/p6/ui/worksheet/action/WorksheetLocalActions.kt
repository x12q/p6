package com.qxdzbc.p6.ui.worksheet.action

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.EdgeSliderAction

/**
 * contains action object for local view within a Worksheet
 */
interface WorksheetLocalActions {
    val verticalScrollBarAction: EdgeSliderAction
    val horizontalScrollBarAction: EdgeSliderAction
}