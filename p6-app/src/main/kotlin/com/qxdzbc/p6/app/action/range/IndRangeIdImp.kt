package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.RangeIdProto

data class IndRangeIdImp(
    override val rangeAddress: RangeAddress,
    override val wbKey: WorkbookKey,
    override val wsName: String
):RangeId {
    override fun toProto():RangeIdProto{
        val proto = RangeIdProto.newBuilder()
            .setRangeAddress(this.rangeAddress.toProto())
            .setWbKey(this.wbKey.toProto())
            .setWsName(this.wsName)
            .build()
        return proto
    }

    companion object{
        fun RangeIdProto.toModel(): IndRangeIdImp {
            return IndRangeIdImp(
                rangeAddress = rangeAddress.toModel(),
                wbKey = wbKey.toModel(),
                wsName = wsName
            )
        }
    }
}



