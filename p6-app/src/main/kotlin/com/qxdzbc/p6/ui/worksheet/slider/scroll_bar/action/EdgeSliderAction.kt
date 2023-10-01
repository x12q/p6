package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.InternalEdgeSliderAction

/**
 * Action of an edge slider. Front facing interface, to be used in the views.
 * This layer serve as an easy-to-implement, so that actions object and views can evolve independently.
 * This layer route the data and action signal to [InternalEdgeSliderAction] where the actual actions happen.
 */
interface EdgeSliderAction {
    fun runAction(actionType: EdgeSliderActionType)
}