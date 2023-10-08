package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.OnDragThumbData

/**
 * Action signal and action data for scroll bar
 */
sealed class ScrollBarActionData{
    class Drag(val data: OnDragThumbData): ScrollBarActionData()
    class ReleaseFromDrag(val data:ReleaseFromDragData): ScrollBarActionData()
}

