package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos.CellProto
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM.Companion.toModel

class CellDM(
    val id: CellIdDM,
    val content:CellContentDM,
) {
    val cellValue get()=content.cellValue
    val formula get()=content.formula
    constructor(
        id: CellIdDM,
        cellValue: CellValue,
        formula:String?
    ):this(
        id = id,
        content = CellContentDM(cellValue, formula)
    )
    companion object CO{
        fun CellProto.toModel(): CellDM {
            return CellDM(
                id = this.id.toModel(),
                cellValue = if(this.hasValue()) this.value.toModel() else CellValue.empty,
                formula = if(this.hasFormula()) this.formula else null
            )
        }
    }
}
