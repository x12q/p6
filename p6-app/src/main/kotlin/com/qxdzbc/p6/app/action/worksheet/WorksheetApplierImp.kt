package com.qxdzbc.p6.app.action.worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.worksheet.delete_cell.applier.DeleteCellResponseApplier
import com.qxdzbc.p6.app.action.worksheet.rename_ws.applier.RenameWorksheetApplier
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponseInterface
import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import javax.inject.Inject

class WorksheetApplierImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val deleteCellResponseApplier: DeleteCellResponseApplier,
    private val renameWorksheetApplier: RenameWorksheetApplier,
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
) : WorksheetApplier {
    var appState by appStateMs

    override fun applyWorkbookUpdate(response: WorkbookUpdateCommonResponseInterface) {
        wbUpdateApplier.apply(response)
    }

    override fun applyRenameWorksheetRs(renameResult: RenameWorksheetResponse): Rse<Unit> {
        return renameWorksheetApplier.applyResRs(renameResult)
    }

    override fun applyDeleteCellResponse(response: DeleteCellResponse){
        deleteCellResponseApplier.applyRes(response)
    }
}
