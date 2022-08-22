package com.qxdzbc.p6.app.action.worksheet.delete_multi

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState

class DeleteMultiResponse2(
    val newWb:Workbook,
    val newWsState:WorksheetState?,
)
