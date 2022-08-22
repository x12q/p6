package com.qxdzbc.p6.app.action.worksheet.delete_cell.applier

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface DeleteCellResponseInternalApplier {
    fun apply(
        workbookKey: WorkbookKey,
        worksheetName: String,
        cellAddress: CellAddress,
        newWorkbook: Workbook?,
    )
}
