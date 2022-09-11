package com.qxdzbc.p6.rpc.document.workbook.msg

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.qxdzbc.common.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.proto.WorkbookProtos

class WorksheetWithErrorReportMsg(
    val wsName: String?,
    val errorReport: ErrorReport?
) {
    companion object {
        fun fromRs(rs: Rse<CreateNewWorksheetResponse2>): WorksheetWithErrorReportMsg {
            when (rs) {
                is Ok -> return WorksheetWithErrorReportMsg(rs.value.newWsName, null)
                is Err -> return WorksheetWithErrorReportMsg(null, rs.error)
            }
        }
    }

    fun toProto(): WorkbookProtos.WorksheetWithErrorReportMsgProto {
        return WorkbookProtos.WorksheetWithErrorReportMsgProto.newBuilder()
            .apply {
                val e = this@WorksheetWithErrorReportMsg.errorReport
                if (e != null) {
                    setErrorReport(e.toProto())
                }
                val w = this@WorksheetWithErrorReportMsg.wsName
                if (w != null) {
                    setWsName(w)
                }
            }.build()
    }
}
