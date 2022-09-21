package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.CellIdProto

/**
 * A direct mapping to [CellIdProto]
 */
data class CellIdDM(
    val address: CellAddress,
    val wbKey:WorkbookKey,
    val wsName:String,
) {
    companion object{
        fun CellIdProto.toModel():CellIdDM{
            return CellIdDM(
                address = this.cellAddress.toModel(),
                wbKey = this.wbKey.toModel(),
                wsName = this.wsName
            )
        }
    }
}
