package com.qxdzbc.p6.rpc.common_data_structure

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM.Companion.toModelDM

/**
 * An independent cell that does not belong to any worksheet
 */
data class IndCellDM(
    val address: CellAddress,
    val content:CellContentDM,
) {
    val value: CellValue get()=content.cellValue
    val formula: String? get() = content.formula
    companion object {
        fun DocProtos.IndCellProto.toModel(): IndCellDM {
            return IndCellDM(
                address = address.toModel(),
                content = content.toModelDM(),
            )
        }
    }
}
