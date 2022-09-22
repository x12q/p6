package com.qxdzbc.p6.app.action.workbook.new_worksheet.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.github.michaelbull.result.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.DocumentContainerMs
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class NewWorksheetApplierImp @Inject constructor(
//    private val errorRouter: ErrorRouter,
    @StateContainerMs private val scMs:Ms<StateContainer>,
    @DocumentContainerMs private val dcMs:Ms<DocumentContainer>,
) : NewWorksheetApplier {

    var sc by scMs
    var dc by dcMs

    override fun applyRes2(rs: RseNav<CreateNewWorksheetResponse>): RseNav<CreateNewWorksheetResponse> {
//        errorRouter.publishIfPossible(rs)
        return rs.map {
            apply(it.newWb)
            it
        }
    }

    fun apply(newWb: Workbook): Rse<Unit> {
        dcMs.value = dc.replaceWb(newWb)
        val rt = sc.getWbStateMsRs(newWb.key)
            .onSuccess { wbStateMs ->
                wbStateMs.value = wbStateMs.value.refreshWsState().setNeedSave(true)
            }
//            .onFailure {
//                errorRouter.publishToApp(it)
//            }
            .map { Unit }
        return rt
    }

}
