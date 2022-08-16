package com.emeraldblast.p6.app.action.workbook.new_worksheet.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import javax.inject.Inject

class NewWorksheetInternalApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val wbStateFactory: WorkbookStateFactory,
) : NewWorksheetInternalApplier {
    var appState by appStateMs
    override fun apply(
        workbookKey: WorkbookKey,
        newWorksheetName: String
    ) {
        appState.queryStateByWorkbookKey(workbookKey).ifOk {
            val workbookStateMs: Ms<WorkbookState> = it.workbookStateMs
            val wbState by workbookStateMs
            val wb = wbState.wb
            if (wb != null) {
                val newWorkbook = wb.createNewWs(newWorksheetName)
                appStateMs.value = appState.replaceWb(newWorkbook)
                workbookStateMs.value = wbState.refreshWsState().setNeedSave(true)
            }
        }
    }

    override fun apply(newWb: Workbook, newWorksheetName: String) {
        appState = appState.replaceWb(newWb)
        val workbookStateMs = appState.getWorkbookStateMs(newWb.key)
        if(workbookStateMs!=null){
            workbookStateMs.value = workbookStateMs.value.refreshWsState().setNeedSave(true)
        }else{
            val newWbStateMs = wbStateFactory.create(ms(newWb))
            appState.globalWbStateCont = appState.globalWbStateCont.addWbState(ms(newWbStateMs))
        }
    }
}
