package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.document_data_layer.Shiftable
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.address.CRAddress
import com.qxdzbc.p6.document_data_layer.cell.address.toModel
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.document_data_layer.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.CellIdProto

/**
 * A direct mapping to [CellIdProto]. This class can store cell id that is completely detacched from the app state.
 */
data class CellIdDM(
    val address: CellAddress,
    override val wbKey:WorkbookKey,
    override val wsName:String,
) :WbWs, Shiftable {

    constructor(address: CellAddress,wbws:WbWs):this(address,wbws.wbKey,wbws.wsName)

    companion object{
        fun CellIdProto.toModel():CellIdDM{
            return CellIdDM(
                address = this.cellAddress.toModel(),
                wbKey = this.wbKey.toModel(),
                wsName = this.wsName
            )
        }
    }
    fun toProto():CellIdProto{
        return CellIdProto.newBuilder()
            .setWbKey(this.wbKey.toProto())
            .setWsName(this.wsName)
            .setCellAddress(this.address.toProto())
            .build()
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): Shiftable {
        return this.copy(address=address.shift(oldAnchorCell,newAnchorCell))
    }
}
