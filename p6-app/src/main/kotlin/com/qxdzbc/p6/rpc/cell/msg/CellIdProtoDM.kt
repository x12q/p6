package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.toModel
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.DocProtos.CellIdProto

data class CellIdProtoDM(
    val address: CellAddress,
    val wbKey:WorkbookKey,
    val wsName:String,
) {
    companion object{
        fun CellIdProto.toModel():CellIdProtoDM{
            return CellIdProtoDM(
                address = this.cellAddress.toModel(),
                wbKey = this.wbKey.toModel(),
                wsName = this.wsName
            )
        }
    }
}
