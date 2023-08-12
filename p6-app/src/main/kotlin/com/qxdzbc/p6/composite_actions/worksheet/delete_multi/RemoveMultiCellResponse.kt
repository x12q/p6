package com.qxdzbc.p6.composite_actions.worksheet.delete_multi

import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.ui.worksheet.state.WorksheetState

class RemoveMultiCellResponse(
    val newWb:Workbook,
    val newWsState:WorksheetState?,
)
