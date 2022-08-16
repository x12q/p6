package com.emeraldblast.p6.app.action.workbook.new_worksheet.applier

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.emeraldblast.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse

interface NewWorksheetApplier {
    fun applyRes2(rs: RseNav<CreateNewWorksheetResponse2>):RseNav<CreateNewWorksheetResponse2>
}
