package com.qxdzbc.p6.composite_actions.worksheet.convert_proto_to_ws

import com.qxdzbc.p6.document_data_layer.worksheet.Worksheet
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto

interface ConvertProtoToWorksheet {
    fun convertProtoToWs(proto: WorksheetProto): Worksheet
}
