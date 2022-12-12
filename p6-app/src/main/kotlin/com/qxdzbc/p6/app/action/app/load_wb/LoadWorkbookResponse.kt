package com.qxdzbc.p6.app.action.app.load_wb

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WindowId_WbKey
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppProtos

data class LoadWorkbookResponse(
    override val errorReport: ErrorReport?,
    override val windowId:String?,
    val wb: Workbook?
) : ResponseWith_WindowId_WbKey {
    fun toProto(): AppProtos.LoadWorkbookResponseProto {
        return AppProtos.LoadWorkbookResponseProto.newBuilder()
            .apply{
                with(this@LoadWorkbookResponse){
                    if(errorReport!=null){
                        setErrorReport(errorReport.toProto())
                    }
                    wb?.key?.toProto()?.also {
                        setWbKey(it)
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


