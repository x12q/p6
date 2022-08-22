package com.qxdzbc.p6.app.action.workbook.new_worksheet.applier

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook

interface NewWorksheetInternalApplier {
    fun apply(
        newWb:Workbook,
        newWorksheetName: String,
    ): Rse<Unit>
}

