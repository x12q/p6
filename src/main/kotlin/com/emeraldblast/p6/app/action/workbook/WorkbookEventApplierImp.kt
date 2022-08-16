package com.emeraldblast.p6.app.action.workbook


import com.emeraldblast.p6.app.action.workbook.add_ws.applier.AddWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.new_worksheet.applier.NewWorksheetApplier
import com.emeraldblast.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.emeraldblast.p6.app.action.workbook.delete_worksheet.DeleteWorksheetResponse
import javax.inject.Inject
@Deprecated("old arc")
class WorkbookEventApplierImp @Inject constructor(
    private val addWsApplier: AddWorksheetApplier,
) : WorkbookEventApplier, AddWorksheetApplier by addWsApplier {

}
