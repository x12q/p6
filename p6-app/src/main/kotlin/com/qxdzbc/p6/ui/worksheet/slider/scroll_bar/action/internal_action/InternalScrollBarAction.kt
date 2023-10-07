package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import com.qxdzbc.p6.ui.worksheet.di.WsComponent
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionType

/**
 * This is meant to be used for reacting to drag action on a thumb of an edge slider.
 * A singleton instance (under [WsScope]) of this one is available within [WsComponent].
 */
interface InternalScrollBarAction {
    /**
     * React to the action of user dragging the thumb of an edge slider
     */
    fun drag(data: ScrollBarActionType.Drag)
}