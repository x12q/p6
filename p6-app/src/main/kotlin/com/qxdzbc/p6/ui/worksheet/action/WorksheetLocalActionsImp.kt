package com.qxdzbc.p6.ui.worksheet.action

import com.qxdzbc.p6.ui.worksheet.di.WsAnvilScope
import com.qxdzbc.p6.ui.worksheet.di.WsScope
import com.qxdzbc.p6.ui.worksheet.slider.edge_slider.action.EdgeSliderAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject


@ContributesBinding(scope= WsAnvilScope::class)
@WsScope
class WorksheetLocalActionsImp @Inject constructor(
    override val edgeSliderAction: EdgeSliderAction
) : WorksheetLocalActions