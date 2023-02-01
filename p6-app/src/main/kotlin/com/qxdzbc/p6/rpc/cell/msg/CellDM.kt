package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM.Companion.toModelDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel
import com.qxdzbc.p6.rpc.common_data_structure.IndependentCellDM

/**
 * This class can store cell data that is completely detached from the app state.
 */
data class CellDM(
    val id: CellIdDM,
    val content:CellContentDM,
) {

    fun toIndCellDM():IndependentCellDM{
        return IndependentCellDM(address=id.address, content=content)
    }

    val cellValue get()=content.cellValue
    val formula get()=content.formula

    companion object {
        fun CellProto.toModel(): CellDM {
            return CellDM(
                id = this.id.toModel(),
                content=this.content.toModelDM()
            )
        }
    }
}
