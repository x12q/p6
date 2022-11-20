package com.qxdzbc.p6.rpc.cell.msg

import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toModel
import com.qxdzbc.p6.proto.DocProtos.CellContentProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * A direct mapping (DM) of [CellContentProto]
 */
data class CellContentDM(
    val cellValue: CellValue = CellValue.empty,
    val formula: String? = null,
    val originalText:String?,
) : CanCheckEmpty {
    companion object {
        fun fromAny(anyValue: Any?): CellContentDM {
            val cv = CellValue.fromAny(anyValue)
            return CellContentDM(
                cellValue = cv,
                originalText =cv.editableValue
            )
        }

        fun fromFormula(formula: String?): CellContentDM {
            return CellContentDM(
                formula = formula,
                originalText = formula,
            )
        }

        fun CellContentProto.toModelDM(): CellContentDM {
            return CellContentDM(
                cellValue = if (hasCellValue()) cellValue.toModel() else CellValue.empty,
                formula = if (hasFormula()) formula else null,
                originalText= if(hasOriginalText()) originalText else null,
            )
        }
    }

    fun toCellContent(translator: P6Translator<ExUnit>): CellContent {
        val rt = formula?.let {
            CellContentImp.fromTransRs(translator.translate(it),originalFormula = this.originalText)
        } ?: CellContentImp(cellValueMs = cellValue.toMs(),originalText = this.originalText)
        return rt
    }

    override fun isEmpty(): Boolean {
        return this.cellValue.isEmpty() && this.formula == null
    }
}
