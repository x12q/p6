package com.emeraldblast.p6.rpc.document.workbook.msg

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.common.proto.toProto
import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.rpc.workbook.WorkbooKServiceProtos.GetAllWorksheetsResponseProto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

class GetAllWorksheetsResponse(
    val worksheets: List<Worksheet>? = null,
    val errorReport: ErrorReport? = null
) {
    companion object{
        fun fromRs(rs:Rse<List<Worksheet>>):GetAllWorksheetsResponse{
            return when (rs){
                is Ok -> GetAllWorksheetsResponse(worksheets = rs.value)
                is Err -> GetAllWorksheetsResponse(errorReport = rs.error)
            }
        }
    }
    fun toProto(): GetAllWorksheetsResponseProto{
        return GetAllWorksheetsResponseProto.newBuilder()
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
