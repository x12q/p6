package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action

import com.qxdzbc.p6.ui.worksheet.di.WsComponent
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ReleaseFromDragData
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.ScrollBarActionData

/**
 * This is meant to be used for reacting to drag action on a thumb of an edge slider.
 * A singleton instance (under [WsScope]) of this one is available within [WsComponent].
 */
interface InternalScrollBarAction {
    /**
     * React to the action of user dragging the thumb of an edge slider
     */
    fun drag(data: ScrollBarActionData.Drag)

    /**
     * React to the action of user release the thumb while dragging it
     */
    fun releaseFromDrag(data: ScrollBarActionData.ReleaseFromDrag)

}