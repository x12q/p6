package com.qxdzbc.p6.app.action.workbook.create_new_ws

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfos.noNav
import com.qxdzbc.p6.app.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorksheetActionImp @Inject constructor(
    private val stateCont: StateContainer,
    private val wbCont: WorkbookContainer,
    private val docCont: DocumentContainer,
) : CreateNewWorksheetAction {

    override fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rse<CreateNewWorksheetResponse> {
        val res: RseNav<CreateNewWorksheetResponse> = makeRequest(request)
        val rt: RseNav<CreateNewWorksheetResponse> = applyRs(res)
        return rt.noNav()
    }

    fun makeRequest(req: CreateNewWorksheetRequest): RseNav<CreateNewWorksheetResponse> {
        val wbk = req.wbKey
        val rs = docCont.getWbRs(wbk).flatMap { wb ->
            wb.addWsRs(req.worksheet).flatMap {
                wb.reRun()
                Ok(CreateNewWorksheetResponse(wb))
            }
        }.mapError { err ->
            err.withNav(wbk)
        }
        return rs
    }

    fun applyRs(res: RseNav<CreateNewWorksheetResponse>): RseNav<CreateNewWorksheetResponse> {
        val rt = res.andThen { addRs ->
            wbCont.overwriteWB(addRs.newWb)
            val wbMs=stateCont.getWbState(addRs.newWb.key)
            wbMs?.refreshWsState()
            Ok(addRs)
        }
        return rt
    }
}
