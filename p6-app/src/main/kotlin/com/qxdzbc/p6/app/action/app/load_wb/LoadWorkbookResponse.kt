package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWithWindowIdAndWorkbookKey
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.AppEventProtos.LoadWorkbookResponseProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.google.protobuf.ByteString

data class LoadWorkbookResponse(
    override val isError: Boolean,
    override val windowId: String?,
    override val errorReport: ErrorReport?,
    val workbook: Workbook?
) : ResponseWithWindowIdAndWorkbookKey {
    companion object {
        fun fromProtoBytes(
            data: ByteString,
            translatorGetter: (wbWsSt: WbWsSt) -> P6Translator<ExUnit>
        ): LoadWorkbookResponse {
            val proto: LoadWorkbookResponseProto = LoadWorkbookResponseProto.newBuilder().mergeFrom(data).build()
            return LoadWorkbookResponse(
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
        get() = this.workbook?.key
}


