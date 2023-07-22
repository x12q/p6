package com.qxdzbc.p6.app.action.app.get_wb

import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.ActionErrors
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class GetWorkbookActionImp @Inject constructor(
    val stateCont: StateContainer
) : GetWorkbookAction {

    val sc = stateCont

    override fun getWbRs(request: GetWorkbookRequest): Rse<Workbook> {
        when{
            request.wbKey!=null ->{
                val rs = sc.getWbRs(request.wbKey)
                return rs
            }
            request.wbName != null->{
                val wbName:String = request.wbName
                for (wb in sc.wbCont.allWbs) {
                    if (wb.key.name == wbName) {
                        return Ok(wb)
                    }
                }
                return WorkbookContainerErrors.InvalidWorkbook.reportDefault(wbName).toErr()
            }
            request.wbIndex!=null ->{
                val index:Int = request.wbIndex
                val wb = sc.wbCont.allWbs.getOrNull(index.toInt())
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
