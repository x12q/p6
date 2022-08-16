package com.emeraldblast.p6.app.action.app.create_new_wb.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.emeraldblast.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookFactory
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import javax.inject.Inject

class CreateNewWbRMImp @Inject constructor(
    @AppStateMs val appStateMs: Ms<AppState>,
    val wbf: WorkbookFactory,
) : CreateNewWbRM {

    private var appState by appStateMs

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
