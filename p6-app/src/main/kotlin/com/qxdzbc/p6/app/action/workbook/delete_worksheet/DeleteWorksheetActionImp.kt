package com.qxdzbc.p6.app.action.workbook.delete_worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier.DeleteWorksheetApplier
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.rm.DeleteWorksheetRM


import com.qxdzbc.p6.rpc.workbook.WorkbookRpcMsgErrors
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.worksheet.msg.WorksheetIdWithIndexPrt
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteWorksheetActionImp @Inject constructor(
    val rm:DeleteWorksheetRM,
    val applier:DeleteWorksheetApplier,
    private val docCont: DocumentContainer,
) : DeleteWorksheetAction {

    private val dc =docCont

    override fun deleteWorksheetRs(request: WorksheetIdWithIndexPrt): Rse<Unit> {
        if (request.wsName != null || request.wsIndex != null) {
            val res = rm.makeRequest(request)
            when(res){
                is Ok ->{
                    if (request.wsName != null) {
                        return applier.applyResRs(request.wsName, res)
                    } else {
                        return dc.getWbRs(request.wbKey)
                            .andThen { wb ->
                                wb.getWsRs(request.wsIndex!!)
                            }.andThen {ws->
                                val wsName = ws.name
                                applier.applyResRs(wsName, res)
                            }
                    }
                }
                is Err ->{
                    // do nothing
                    return res
                }
            }

        } else {
            return WorkbookRpcMsgErrors.IllegalMsg.report("IdentifyWorksheetMsg must contain at least a worksheet name or a worksheet index")
                .toErr()
        }
    }
}
