package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.proto.WorksheetProtos


data class CellCountResponse(val count: Long) {
    fun toProto(): WorksheetProtos.CellCountResponseProto {
        return WorksheetProtos.CellCountResponseProto.newBuilder().setCount(count).build()
    }
}
