package com.qxdzbc.p6.composite_actions.range

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos

data class RangeIdImp(
    override val rangeAddress: RangeAddress,
    private val wbKeySt: St<WorkbookKey>,
    private val wsNameSt: St<String>
): RangeId {
    override val wbKey: WorkbookKey
        get()  = wbKeySt.value
    override val wsName: String
        get()  = wsNameSt.value

    override fun toProto(): DocProtos.RangeIdProto {
        val proto = DocProtos.RangeIdProto.newBuilder()
            .setRangeAddress(this.rangeAddress.toProto())
            .setWbKey(this.wbKey.toProto())
            .setWsName(this.wsName)
            .build()
        return proto
    }

    override fun toDm(): RangeId {
        return RangeIdDM(rangeAddress, wbKey, wsName)
    }
}
