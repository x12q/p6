package com.emeraldblast.p6.rpc.document.workbook.msg

import com.emeraldblast.p6.app.document.worksheet.Worksheet
import com.emeraldblast.p6.proto.rpc.workbook.WorkbooKServiceProtos.GetWorksheetResponseProto

class GetWorksheetResponse(
    val worksheet: Worksheet?
) {
    fun toProto(): GetWorksheetResponseProto{
        return GetWorksheetResponseProto.newBuilder()
            .apply {
                this@GetWorksheetResponse.worksheet?.also {
                    setWorksheet(it.toProto())
                }
            }.build()
    }
}
