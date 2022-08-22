package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardAction
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiAction
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState

interface WorksheetAction : WorksheetAction2, RangeToClipboardAction, DeleteMultiAction,
    RestoreWindowFocusState
