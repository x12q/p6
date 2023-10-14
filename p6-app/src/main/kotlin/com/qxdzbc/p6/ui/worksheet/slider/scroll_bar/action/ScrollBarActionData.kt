package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.OnDragThumbData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarState

/**
 * Action signal and action data for scroll bar
 */
sealed class ScrollBarActionData{
    class Drag(val state:ScrollBarState): ScrollBarActionData()
    class ReleaseFromDrag(val state: ScrollBarState): ScrollBarActionData()
    data class ClickOnRail(val clickPosition:Float,val state: ScrollBarState):ScrollBarActionData()
}

