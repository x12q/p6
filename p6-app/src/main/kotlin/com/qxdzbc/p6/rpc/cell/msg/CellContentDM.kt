package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.d.CellContent
import com.qxdzbc.p6.app.document.cell.d.CellContentImp
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.app.document.cell.d.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.CellProtos.CellContentProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * A direct mapping (DM) of [CellContentProto]
 */
data class CellContentDM(
    val cellValue:CellValue = CellValue.empty,
    val formula:String? = null
) :CanCheckEmpty{
    companion object{
        fun fromAny(anyValue:Any?):CellContentDM{
            return CellContentDM(
                cellValue = CellValue.fromAny(anyValue)
            )
        }
        fun fromFormula(formula:String?):CellContentDM{
            return CellContentDM(
             formula = formula
            )
        }
        fun CellContentProto.toModel():CellContentDM{
            return CellContentDM(
                cellValue = if(hasCellValue()) cellValue.toModel() else  CellValue.empty,
                formula = if(hasFormula()) formula else null
            )
        }

    }


    fun toStateObj(translator:P6Translator<ExUnit>):CellContent{
        val rt = formula?.let {
            CellContentImp.fromTransRs(translator.translate(it))
        } ?: CellContentImp(cellValueMs = cellValue.toMs())
        return rt
    }

    override fun isEmpty(): Boolean {
        return this.cellValue.isEmpty() && this.formula==null
    }
}
