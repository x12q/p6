package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId
import com.qxdzbc.p6.rpc.worksheet.msg.IndeCellId.Companion.toIndeModel

class CellProtoDM(
    val id: IndeCellId,
    val content:CellContentProtoDM,
) {
    val cellValue get()=content.cellValue
    val formula get()=content.formula
    constructor(
         id: IndeCellId,
         cellValue:CellValue,
         formula:String?
    ):this(
        id = id,
        content = CellContentProtoDM(cellValue, formula)
    )
    companion object CO{
        fun CellProto.toModel(): CellProtoDM {
            return CellProtoDM(
                id = this.id.toIndeModel(),
                cellValue = if(this.hasValue()) this.value.toModel() else CellValue.empty,
                formula = if(this.hasFormula()) this.formula else null
            )
        }
    }
}
