package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId.Companion.toIndeModel

class CellPrt(
    val id: IndeCellId,
    val cellValue:CellValue,
    val formula:String?
) {
    companion object CO{
        fun CellProto.toModel(): CellPrt {
            return CellPrt(
                id = this.id.toIndeModel(),
                cellValue = if(this.hasValue()) this.value.toModel() else CellValue.empty,
                formula = if(this.hasFormula()) this.formula else null
            )
        }
    }
}
