package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action

import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.ClickOnRailAction
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.DragThumb
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.action.internal_action.ReleaseThumbFromDrag
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@WsScope
@ContributesBinding(scope = WsAnvilScope::class)
class ScrollBarActionImp @Inject constructor(
    private val dragAction: DragThumb,
    private val releaseFromDrag: ReleaseThumbFromDrag,
    private val clickOnRail: ClickOnRailAction,
) : ScrollBarAction {

    override fun runAction(data: ScrollBarActionData) {
        when (data) {
            is ScrollBarActionData.Drag -> dragAction.run(data)
            is ScrollBarActionData.ReleaseFromDrag -> releaseFromDrag.run(data)
            is ScrollBarActionData.ClickOnRail -> clickOnRail.run(data)
        }
    }
}