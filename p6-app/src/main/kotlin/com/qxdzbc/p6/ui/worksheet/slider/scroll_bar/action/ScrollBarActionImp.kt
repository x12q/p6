package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.InternalScrollBarAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class ScrollBarActionImp @Inject constructor(
    private val internalEdgeSliderAction: InternalScrollBarAction
) : ScrollBarAction {

    override fun runAction(data: ScrollBarActionData) {
        when (data) {
            is ScrollBarActionData.Drag -> internalEdgeSliderAction.drag(data)
            is ScrollBarActionData.ReleaseFromDrag -> internalEdgeSliderAction.releaseFromDrag(data.data)
        }
    }
}