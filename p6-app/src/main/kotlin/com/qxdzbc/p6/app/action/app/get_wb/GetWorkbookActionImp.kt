package com.qxdzbc.p6.app.action.app.get_wb

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.action.ActionErrors
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.proto.AppProtos
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class GetWorkbookActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>
) : GetWorkbookAction {

    val sc by stateContSt

    override fun getWbRs(request: GetWorkbookRequest): Rse<Workbook> {
        when{
            request.wbKey!=null ->{
                val rs = sc.getWbRs(request.wbKey)
                return rs
            }
            request.wbName != null->{
                val wbName:String = request.wbName
                for (wb in sc.wbCont.wbList) {
                    if (wb.key.name == wbName) {
                        return Ok(wb)
                    }
                }
                return WorkbookContainerErrors.InvalidWorkbook.reportDefault(wbName).toErr()
            }
            request.wbIndex!=null ->{
                val index:Int = request.wbIndex
                val wb = sc.wbCont.wbList.getOrNull(index.toInt())
                if (wb != null) {
                    return Ok(wb)
                } else {
                    return WorkbookContainerErrors.InvalidWorkbook.report(index.toInt()).toErr()
                }
            }
            else ->{
                return ActionErrors.InvalidRequest.report("Can't get workbook with request ${request}. GetWorkbookRequest must have at least one non-null property").toErr()
            }
        }
    }
}
