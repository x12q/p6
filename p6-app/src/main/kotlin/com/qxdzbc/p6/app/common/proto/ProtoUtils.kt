package com.qxdzbc.p6.app.common.proto

import com.qxdzbc.p6.common.exception.error.ErrorHeader
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.proto.CommonProtos
object ProtoUtils {
    fun CommonProtos.ErrorReportProto.toModel(): ErrorReport {
        return ErrorReport(
            ErrorHeader(
                errorCode = errorCode,
                errorDescription = errorMessage
            ),
            data=if(this.hasData()) data else "",
        )
    }

    fun ErrorReport.toProto(): CommonProtos.ErrorReportProto {
        val rt = CommonProtos.ErrorReportProto.newBuilder()
            .setErrorCode(header.errorCode)
            .setErrorMessage(header.errorDescription)
            .build()
        return rt
    }
}
