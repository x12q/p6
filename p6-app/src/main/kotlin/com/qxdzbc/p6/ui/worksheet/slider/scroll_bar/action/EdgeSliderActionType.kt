package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.OnDragThumbData

sealed class EdgeSliderActionType{
    class Drag(val data: OnDragThumbData): EdgeSliderActionType()
}