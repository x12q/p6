package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.compose.StateUtils.ms
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
class CellContentProtoDM(
    val cellValue:CellValue = CellValue.empty,
    val formula:String? = null
) :CanCheckEmpty{

    fun toStateObj(translator:P6Translator<ExUnit>):CellContent{
        val rt = formula?.let {
            CellContentImp.fromTransRs(translator.translate(it))
        } ?: CellContentImp(cellValueMs = cellValue.toMs())
        return rt
    }
    companion object{
        fun CellContentProto.toModel():CellContentProtoDM{
            return CellContentProtoDM(
                cellValue = if(hasCellValue()) cellValue.toModel() else  CellValue.empty,
                formula = if(hasFormula()) formula else null
            )
        }
    }

    override fun isEmpty(): Boolean {
        return this.cellValue.isEmpty() && this.formula==null
    }
}
