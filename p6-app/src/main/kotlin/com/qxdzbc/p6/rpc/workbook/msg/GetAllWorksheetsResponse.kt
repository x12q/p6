package com.qxdzbc.p6.rpc.workbook.msg

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rse
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.proto.WorkbookProtos

class GetAllWorksheetsResponse(
    val worksheets: List<Worksheet>? = null,
    val errorReport: ErrorReport? = null
) {
    companion object{
        fun fromRs(rs: Rse<List<Worksheet>>): GetAllWorksheetsResponse {
            return when (rs){
                is Ok -> GetAllWorksheetsResponse(worksheets = rs.value)
                is Err -> GetAllWorksheetsResponse(errorReport = rs.error)
            }
        }
    }
    fun toProto(): WorkbookProtos.GetAllWorksheetsResponseProto {
        return WorkbookProtos.GetAllWorksheetsResponseProto.newBuilder()
            .apply {
                val wsl = this@GetAllWorksheetsResponse.worksheets
                if(wsl!=null){
                    addAllWorksheets(wsl.map{it.toProto()})
                }
            }
            .apply {
                this@GetAllWorksheetsResponse.errorReport?.also {
                    this.setErrorReport(it.toProto())
                }
            }.build()
    }
}
