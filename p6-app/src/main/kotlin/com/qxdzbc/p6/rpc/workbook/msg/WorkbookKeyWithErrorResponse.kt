package com.qxdzbc.p6.rpc.workbook.msg

import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.AppProtos.WorkbookKeyWithErrorResponseProto

class WorkbookKeyWithErrorResponse(
    val wbKey: WorkbookKey?,
    val errorReport: ErrorReport?
) {
    fun toProto(): WorkbookKeyWithErrorResponseProto {
        return WorkbookKeyWithErrorResponseProto.newBuilder()
            .apply {
                with(this@WorkbookKeyWithErrorResponse){
                    errorReport?.toProto()?.also {
                        setErrorReport(it)
                    }
                    wbKey?.toProto()?.also {
                        setWbKey(it)
                    }
                }
            }
            .build()
    }
}
