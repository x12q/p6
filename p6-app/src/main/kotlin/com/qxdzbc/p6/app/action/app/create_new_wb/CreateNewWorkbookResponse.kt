package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.AppEventProtos.CreateNewWorkbookResponseProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString

data class CreateNewWorkbookResponse(
    override val isError: Boolean,
    override val errorReport: ErrorReport?,
    val workbook: Workbook?,
    override val windowId: String?
) : ResponseWithWindowIdAndWorkbookKey {
    companion object {
        fun fromProtoBytes(
            data: ByteString,
            translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>
        ): CreateNewWorkbookResponse {
            val proto = CreateNewWorkbookResponseProto.newBuilder().mergeFrom(data).build()
            return CreateNewWorkbookResponse(
                isError = proto.isError,
                errorReport = if (proto.hasErrorReport()) proto.errorReport.toModel() else null,
                workbook = if (proto.hasWorkbook()) proto.workbook.toModel(translatorGetter) else null,
                windowId = if (proto.hasWindowId()) proto.windowId else null
            )
        }
    }

    override fun isLegal(): Boolean {
        return true
    }

    override val wbKey: WorkbookKey?
        get() = workbook?.key
}
