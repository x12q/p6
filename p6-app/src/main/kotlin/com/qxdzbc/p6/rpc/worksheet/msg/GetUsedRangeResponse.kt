package com.qxdzbc.p6.rpc.worksheet.msg

import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.proto.WorksheetProtos

class GetUsedRangeResponse(
    val ra: RangeAddress? = null
) {
    fun toProto(): WorksheetProtos.GetUsedRangeResponseProto {
        return ra?.let {
            WorksheetProtos.GetUsedRangeResponseProto.newBuilder().setRangeAddress(it.toProto()).build()
        } ?: WorksheetProtos.GetUsedRangeResponseProto.getDefaultInstance()
    }
}
