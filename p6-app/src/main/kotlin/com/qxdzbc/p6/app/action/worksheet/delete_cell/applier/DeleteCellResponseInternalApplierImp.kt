package com.qxdzbc.p6.app.action.worksheet.delete_cell.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.QueryByWorkbookKeyResult
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteCellResponseInternalApplierImp
@Inject constructor(
    val appState:AppState,
    val docContMs:Ms<DocumentContainer>,
) : DeleteCellResponseInternalApplier {

    private var dc by docContMs

    override fun apply(
        workbookKey: WorkbookKey,
        worksheetName: String,
        cellAddress: CellAddress,
        newWorkbook: Workbook?
    ) {
        val queryRs: QueryByWorkbookKeyResult = appState.queryStateByWorkbookKey(workbookKey)
        queryRs.ifOk {
            val newWb = newWorkbook
            if (newWb != null) {
                it.workbookStateMs.value =
                    it.workbookStateMs.value.setWorkbookKeyAndRefreshState(newWb.key).setNeedSave(true)
                dc = dc.replaceWb(newWb)
            }
        }
    }
}
