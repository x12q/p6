package com.qxdzbc.p6.rpc.document.worksheet.msg

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.proto.rpc.worksheet.WorksheetServiceProtos

class GetUsedRangeResponse(
    val ra: RangeAddress? = null
) {
    fun toProto(): WorksheetServiceProtos.GetUsedRangeResponseProto {
        return ra?.let {
            WorksheetServiceProtos.GetUsedRangeResponseProto.newBuilder().setRangeAddress(it.toProto()).build()
        } ?: WorksheetServiceProtos.GetUsedRangeResponseProto.getDefaultInstance()
    }
}
