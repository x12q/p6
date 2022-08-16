package com.emeraldblast.p6.app.action.workbook.new_worksheet

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.emeraldblast.p6.common.exception.error.ErrorReport

interface NewWorksheetAction {
    fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rs<CreateNewWorksheetResponse2, ErrorReport>
}
