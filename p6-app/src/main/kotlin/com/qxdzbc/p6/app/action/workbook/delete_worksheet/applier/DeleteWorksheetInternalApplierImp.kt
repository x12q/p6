package com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse

import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.github.michaelbull.result.flatMap
import javax.inject.Inject

class DeleteWorksheetInternalApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val wbStateFactory: WorkbookStateFactory
) : DeleteWorksheetInternalApplier {
    var appState by appStateMs

    override fun applyRs(targetSheetName:String,rs: Rse<Workbook>): Rse<Unit> {
        return rs.flatMap { newWB->
            // x: update wb
            appState = appState.replaceWb(newWB)
            // x: update wb state
            val wbKey = newWB.key
            val wbStateMs = appState.getWbStateMs(wbKey)
            if(wbStateMs!=null){
                val newWbState=wbStateMs.value.refresh().setNeedSave(true)
                wbStateMs.value = newWbState
            }
            //update translator map
            appState.translatorContainer = appState.translatorContainer.removeTranslator(wbKey,targetSheetName)
            Unit.toOk()
        }
    }
}
