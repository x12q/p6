package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.InternalEdgeSliderAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class EdgeSliderActionImp @Inject constructor(
    private val internalEdgeSliderAction: InternalEdgeSliderAction
) : EdgeSliderAction {

    override fun runAction(actionType: EdgeSliderActionType) {
        when (actionType) {
            is EdgeSliderActionType.Drag -> internalEdgeSliderAction.drag(actionType)
        }
    }
}