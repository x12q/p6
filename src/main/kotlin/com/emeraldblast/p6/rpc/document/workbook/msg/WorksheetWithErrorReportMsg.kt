package com.emeraldblast.p6.rpc.document.workbook.msg

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.common.proto.toProto
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.rpc.workbook.WorkbooKServiceProtos
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

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

    fun toProto(): WorkbooKServiceProtos.WorksheetWithErrorReportMsgProto {
        return WorkbooKServiceProtos.WorksheetWithErrorReportMsgProto.newBuilder()
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
