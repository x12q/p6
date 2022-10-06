package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb.action

import javax.inject.Inject

class ThumbActionImp @Inject constructor(
    private val dragThumbAct: DragThumbAction
) : ThumbAction,
    DragThumbAction by dragThumbAct
