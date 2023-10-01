package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.internal_action

import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.EdgeSliderActionType

/**
 * This is meant to be used for reacting to drag action on a thumb of an edge slider.
 * A singleton instance of this one is available in [WsScope].
 */
interface InternalEdgeSliderAction {
    /**
     * React to the action of user dragging the thumb of an edge slider
     */
    fun drag(data: EdgeSliderActionType.Drag)
}