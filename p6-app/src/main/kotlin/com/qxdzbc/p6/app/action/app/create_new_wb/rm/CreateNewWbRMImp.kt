package com.qxdzbc.p6.app.action.app.create_new_wb.rm

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
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
