package com.qxdzbc.p6.ui.window.tool_bar.action

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.action.TextColorSelectorActionQ
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.tool_bar.color_selector.action.ColorSelectorAction
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.action.TextSizeSelectorAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ToolBarActionImp @Inject constructor(
    override val textSizeSelectorAction: TextSizeSelectorAction,
    @TextColorSelectorActionQ
    override val textColorSelectorAction: ColorSelectorAction,
) : ToolBarAction
