package com.qxdzbc.p6.app.action.app.create_new_wb.rm

import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import javax.inject.Inject

class CreateNewWbRMImp @Inject constructor(
    private val wbf: WorkbookFactory,
) : CreateNewWbRM {

    override fun createNewWb(request: CreateNewWorkbookRequest): CreateNewWorkbookResponse {
        val newWbRs = wbf.createWbRs(request.wbName)
        when(newWbRs){
            is Ok -> {
                return CreateNewWorkbookResponse(
                    errorReport = null,
                    wb =newWbRs.value,
                    windowId = request.windowId
                )
            }
            is Err ->{
                return CreateNewWorkbookResponse(
                    errorReport = newWbRs.error,
                    wb =null,
                    windowId = request.windowId
                )
            }
        }
    }
}
