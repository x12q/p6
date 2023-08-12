package com.qxdzbc.p6.composite_actions.range

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos

interface RangeId: WbWs {
    val rangeAddress: RangeAddress
    fun toProto(): DocProtos.RangeIdProto
    fun toDm():RangeId
}
