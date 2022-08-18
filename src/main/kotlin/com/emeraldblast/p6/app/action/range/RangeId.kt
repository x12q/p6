package com.emeraldblast.p6.app.action.range

import com.emeraldblast.p6.app.action.common_data_structure.WbWs
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses.toModel
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.proto.DocProtos.RangeIdProto

data class RangeId(
    val rangeAddress: RangeAddress,
    override val wbKey: WorkbookKey,
    override val wsName: String
):WbWs {
    fun toProto():RangeIdProto{
        val proto = RangeIdProto.newBuilder()
            .setRangeAddress(this.rangeAddress.toProto())
            .setWorkbookKey(this.wbKey.toProto())
            .setWorksheetName(this.wsName)
            .build()
        return proto
    }

    companion object{
        fun RangeIdProto.toModel(): RangeId {
            return RangeId(
                rangeAddress = rangeAddress.toModel(),
                wbKey = workbookKey.toModel(),
                wsName = worksheetName
            )
        }
    }
}



