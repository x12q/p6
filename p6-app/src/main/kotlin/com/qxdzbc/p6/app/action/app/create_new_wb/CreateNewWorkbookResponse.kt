package com.qxdzbc.p6.app.action.app.create_new_wb

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WindowId_WbKey
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppProtos.CreateNewWorkbookResponseProto

data class CreateNewWorkbookResponse(
    override val errorReport: ErrorReport?,
    val wb: Workbook?,
    override val windowId: String?
) : ResponseWith_WindowId_WbKey {
    fun toProto(): CreateNewWorkbookResponseProto {
        return CreateNewWorkbookResponseProto.newBuilder()
            .apply {
                with(this@CreateNewWorkbookResponse){
                    errorReport?.toProto()?.also {
                        setErrorReport(it)
                    }
                    wb?.key?.toProto()?.also {
                        setWbKey(it)
                    }
                    windowId?.also {
                        setWindowId(it)
                    }
                }
            }
            .build()
    }

    override val isError: Boolean
        get() = errorReport != null
    override val wbKey: WorkbookKey?
        get() = wb?.key

}
