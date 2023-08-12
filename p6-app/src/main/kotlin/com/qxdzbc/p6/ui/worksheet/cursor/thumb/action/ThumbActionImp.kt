package com.qxdzbc.p6.ui.worksheet.cursor.thumb.action

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.composite_actions.cursor.thumb.drag_thumb_action.DragThumbAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class, boundType=ThumbAction::class)
class ThumbActionImp @Inject constructor(
    private val dragThumbAct: DragThumbAction
) : ThumbAction,
    DragThumbAction by dragThumbAct
