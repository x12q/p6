package com.emeraldblast.p6.app.action.workbook.new_worksheet.applier

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.Workbook

interface NewWorksheetInternalApplier {
    fun apply(
        newWb:Workbook,
        newWorksheetName: String,
    ): Rse<Unit>
}

