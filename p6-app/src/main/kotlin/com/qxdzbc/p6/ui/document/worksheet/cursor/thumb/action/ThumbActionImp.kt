package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType=ThumbAction::class)
class ThumbActionImp @Inject constructor(
    private val dragThumbAct: DragThumbAction
) : ThumbAction,
    DragThumbAction by dragThumbAct
