package com.qxdzbc.p6.app.action.app.create_new_wb.rm

import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.Fake
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
@Fake
class FakeNewWbRM @Inject constructor(): CreateNewWbRM {
    private var counter=0
    override fun createNewWb(request: CreateNewWorkbookRequest): CreateNewWorkbookResponse {
        val rt= CreateNewWorkbookResponse(
            errorReport = null,
            wb = WorkbookImp(
                keyMs = WorkbookKey("FakeBook${counter}").toMs(),
            ).apply {
                    this.createNewWs("Sheet1")
            },
            windowId = null
        )
        counter++
        return rt
    }
}
