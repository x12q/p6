package com.qxdzbc.p6.app.action.worksheet.convert_proto_to_ws

import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto

interface ConvertProtoToWorksheet {
    fun convertProtoToWs(proto: WorksheetProto): Worksheet
}
