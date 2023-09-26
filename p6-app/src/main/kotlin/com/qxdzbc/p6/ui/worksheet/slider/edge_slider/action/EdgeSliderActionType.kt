package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action

import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.OnDragThumbData

sealed class EdgeSliderActionType{
    class Drag(val data: OnDragThumbData): EdgeSliderActionType()
}