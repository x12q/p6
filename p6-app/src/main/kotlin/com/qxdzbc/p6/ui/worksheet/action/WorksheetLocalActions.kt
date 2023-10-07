package com.qxdzbc.p6.ui.worksheet.action

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarAction

/**
 * contains action object for local view within a Worksheet
 */
interface WorksheetLocalActions {
    val verticalScrollBarAction: ScrollBarAction
    val horizontalScrollBarAction: ScrollBarAction
}