package com.qxdzbc.p6.rpc.document.cell.msg

import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos.Cell2Proto
import com.qxdzbc.p6.rpc.document.worksheet.msg.CellId
import com.qxdzbc.p6.rpc.document.worksheet.msg.CellId.Companion.toModel

class Cell2Prt(
    val id:CellId,
    val cellValue:CellValue,
    val formula:String?
) {
    companion object CO{
        fun Cell2Proto.toModel():Cell2Prt{
            return Cell2Prt(
                id = this.id.toModel(),
                cellValue = if(this.hasValue()) this.value.toModel() else CellValue.empty,
                formula = if(this.hasFormula()) this.formula else null
            )
        }
    }
}
