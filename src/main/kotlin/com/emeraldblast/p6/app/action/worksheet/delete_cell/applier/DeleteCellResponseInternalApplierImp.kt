package com.emeraldblast.p6.app.action.worksheet.delete_cell.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.di.state.app_state.AppStateMs
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.app.state.AppState
import com.emeraldblast.p6.ui.app.state.QueryByWorkbookKeyResult
import com.emeraldblast.p6.ui.common.compose.Ms
import javax.inject.Inject

class DeleteCellResponseInternalApplierImp
@Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
) : DeleteCellResponseInternalApplier {
    var appState by appStateMs
    override fun apply(
        workbookKey: WorkbookKey,
        worksheetName: String,
        cellAddress: CellAddress,
        newWorkbook: Workbook?
    ) {
        val queryRs: QueryByWorkbookKeyResult = appStateMs.value.queryStateByWorkbookKey(workbookKey)
        queryRs.ifOk {
            val newWb = newWorkbook
            if (newWb != null) {
                it.workbookStateMs.value =
                    it.workbookStateMs.value.setWorkbookKeyAndRefreshState(newWb.key).setNeedSave(true)
                appState = appState.replaceWb(newWb)
            }
        }
    }
}
