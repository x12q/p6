package com.qxdzbc.p6.composite_actions.workbook.create_new_ws

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.mapError
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorksheetActionImp @Inject constructor(
    private val stateCont: StateContainer,
    private val docCont: DocumentContainer,
) : CreateNewWorksheetAction {

    override fun createNewWorksheetRs(request: CreateNewWorksheetRequest): Rse<CreateNewWorksheetResponse> {
        val wbk = request.wbKey
        val rs = docCont.getWbRs(wbk).flatMap { wb ->
            wb.addWsRs(request.worksheet).flatMap {
                wb.reRun()
                Ok(CreateNewWorksheetResponse(wb))
            }
        }.mapError { err ->
            err
        }

        val rt = rs.andThen { addRs ->
            val wbMs=stateCont.getWbState(addRs.newWb.key)
            wbMs?.refreshWsState()
            Ok(addRs)
        }
        return rt
    }
}
