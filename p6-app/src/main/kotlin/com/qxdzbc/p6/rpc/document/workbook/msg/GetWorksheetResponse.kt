package com.qxdzbc.p6.rpc.document.workbook.msg

import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.proto.rpc.workbook.WorkbooKServiceProtos.GetWorksheetResponseProto

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
