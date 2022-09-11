package com.qxdzbc.p6.rpc.document.workbook.msg

import com.qxdzbc.p6.proto.WorksheetProtos
import com.qxdzbc.p6.rpc.document.worksheet.msg.WorksheetIdPrt
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId

class GetWorksheetResponse(
    val wsId: WorksheetId?
) {
    fun toProto(): WorksheetProtos.GetWorksheetResponseProto {
        return WorksheetProtos.GetWorksheetResponseProto.newBuilder()
            .apply {
                this@GetWorksheetResponse.wsId?.also {
                    setWsId(it.toProto())
                }
            }.build()
    }
}
