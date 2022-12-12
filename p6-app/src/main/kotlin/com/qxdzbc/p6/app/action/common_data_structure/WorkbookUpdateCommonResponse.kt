package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WbKey
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.WorkbookProtos.*
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

open class WorkbookUpdateCommonResponse(
    override val errorReport: ErrorReport? = null,
    override val wbKey: WorkbookKey? = null,
    override val newWorkbook: Workbook? = null,
    override val windowId: String? = null,
) : ResponseWith_WbKey, WorkbookUpdateCommonResponseInterface {
    override val isError: Boolean get()=errorReport!=null
    companion object {
        fun WorkbookUpdateCommonResponseProto.toModel(
            translatorGetter: (wbWsSt:WbWsSt) -> P6Translator<ExUnit>
        ): WorkbookUpdateCommonResponse {
            return WorkbookUpdateCommonResponse(
                errorReport = if (this.hasErrorReport()) errorReport.toModel() else null,
                wbKey = if (this.hasWorkbookKey()) workbookKey.toModel() else null,
                newWorkbook = if (this.hasNewWorkbook()) newWorkbook.toShallowModel(translatorGetter) else null,
                windowId = if (this.hasWindowId()) this.windowId else null,
            )
        }
    }
}
