package com.qxdzbc.p6.composite_actions.common_data_structure

import com.qxdzbc.p6.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.rpc.communication.res_req_template.response.ResponseWith_WbKey
import com.qxdzbc.p6.document_data_layer.workbook.Workbook
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
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
