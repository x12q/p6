package com.qxdzbc.p6.app.action.app.create_new_wb.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.workbook.WorkbookFactory
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import javax.inject.Inject

class CreateNewWbRMImp @Inject constructor(
    val wbf: WorkbookFactory,
) : CreateNewWbRM {

    override fun createNewWb(request: CreateNewWorkbookRequest): CreateNewWorkbookResponse {
        val newWbRs = wbf.createWbRs()
        when(newWbRs){
            is Ok -> {
                return CreateNewWorkbookResponse(
                    isError = false,
                    errorReport = null,
                    workbook =newWbRs.value,
                    windowId = request.windowId
                )
            }
            is Err ->{
                return CreateNewWorkbookResponse(
                    isError = true,
                    errorReport = newWbRs.error,
                    workbook =null,
                    windowId = request.windowId
                )
            }
        }
    }
}
