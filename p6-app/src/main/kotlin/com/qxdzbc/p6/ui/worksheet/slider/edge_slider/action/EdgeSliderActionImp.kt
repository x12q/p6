package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action

import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.internal_action.InternalEdgeSliderAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class EdgeSliderActionImp @Inject constructor(
    private val internalEdgeSliderAction: InternalEdgeSliderAction
) : EdgeSliderAction {

    override fun onAction(actionType: EdgeSliderActionType) {
        when (actionType) {
            is EdgeSliderActionType.Drag -> internalEdgeSliderAction.run(actionType)
        }
    }
}