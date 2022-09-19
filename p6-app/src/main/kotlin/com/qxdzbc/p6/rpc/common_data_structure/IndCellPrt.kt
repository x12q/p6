package com.qxdzbc.p6.rpc.common_data_structure

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos

data class IndCellPrt(
    val address: CellAddress,
    val value: CellValue? = null,
    val formula: String? = null,
) {
    companion object {
        fun DocProtos.IndCellProto.toModel(): IndCellPrt {
            return IndCellPrt(
                address = address.toModel(),
                value = if (this.hasValue()) value.toModel() else null,
                formula = if (this.hasFormula()) formula else null
            )
        }
    }
}
