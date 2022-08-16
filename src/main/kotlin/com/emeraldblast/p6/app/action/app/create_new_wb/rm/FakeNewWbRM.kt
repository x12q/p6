package com.emeraldblast.p6.app.action.app.create_new_wb.rm

import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.emeraldblast.p6.app.document.workbook.WorkbookImp
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import javax.inject.Inject

class FakeNewWbRM @Inject constructor(): CreateNewWbRM {
    private var counter=0
    override fun createNewWb(request: CreateNewWorkbookRequest): CreateNewWorkbookResponse {
        val rt= CreateNewWorkbookResponse(
            isError = false,
            errorReport = null,
            workbook = WorkbookImp(
                keyMs = WorkbookKey("FakeBook${counter}").toMs(),
            ).apply {
                    this.createNewWs("Sheet1")
            },
            windowId = null
        )
        counter++
        return rt
    }
}
