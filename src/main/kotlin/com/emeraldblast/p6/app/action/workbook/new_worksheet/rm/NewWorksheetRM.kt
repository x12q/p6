package com.emeraldblast.p6.app.action.workbook.new_worksheet.rm

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.emeraldblast.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.emeraldblast.p6.app.document.workbook.Workbook

interface NewWorksheetRM {
    fun newWorksheet2(request: CreateNewWorksheetRequest): RseNav<CreateNewWorksheetResponse2>
}

data class CreateNewWorksheetResponse2(
    val newWb: Workbook,
    val newWsName: String,
)
