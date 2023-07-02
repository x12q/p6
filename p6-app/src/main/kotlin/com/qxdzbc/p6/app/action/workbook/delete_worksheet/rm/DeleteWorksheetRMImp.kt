package com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.Workbook

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.rpc.workbook.WorkbookRpcMsgErrors
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteWorksheetRMImp @Inject constructor(
    private val docCont: DocumentContainer,
) : DeleteWorksheetRM {

    private val dc = docCont

    override fun makeRequest(request: WorksheetIdWithIndexPrt): Rse<Workbook> {
        val wbKey = request.wbKey
        val name = request.wsName
        val index = request.wsIndex
        if (name != null) {
            return this.deleteWorksheetRs(wbKey, name)
        } else if (index != null) {
            return this.deleteWorksheetRs(wbKey, index)
        } else {
            return WorkbookRpcMsgErrors.IllegalMsg
                .report("IdentifyWorksheetMsg must contain at least a worksheet name or a worksheet index")
                .toErr()
        }
    }

     private fun deleteWorksheetRs(wbKey: WorkbookKey, wsName: String): Rse<Workbook> {
        val wbRs = dc.getWbRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsName).map { it.reRun() }
        }
        return rt
    }

    private fun deleteWorksheetRs(wbKey: WorkbookKey, wsIndex: Int): Rse<Workbook> {
        val wbRs = dc.getWbRs(wbKey)
        val rt = wbRs.flatMap { wb ->
            wb.removeSheetRs(wsIndex).map { it.reRun() }
        }
        return rt
    }
}
