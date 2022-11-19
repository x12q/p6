package com.qxdzbc.p6.app.document.cell

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.CellContentProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.formula.execution_unit.IntUnit
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

/**
 * Content = Formula + value
 * A class that holds content (value and formula) of a cell.
 * This implementation hold a mutable [CellValue] instance([cellValueMs]), whenever [cellValueAfterRun] is access, a new instance of cell value is computed. This is for auto formula computation
 */
data class CellContentImp(
    private val cellValueMs: Ms<CellValue> = ms(CellValue.empty),
    override val exUnit: ExUnit? = null,
    // TODO set to default null temporarily, until all the related code is updated
    override val originalText: String? = null,
) : CellContent {
    override val fullFormula: String?
        get() = exUnit?.toFormula()?.let {
            "=" + it
        }

    override fun shortFormula(wbKey: WorkbookKey?, wsName: String?): String? {
        return exUnit?.toShortFormula(wbKey, wsName)?.let {
            "=" + it
        }
    }

    override fun colorFormula(colorMap: ColorMap, wbKey: WorkbookKey?, wsName: String?): AnnotatedString? {
        return exUnit?.toColorFormula(colorMap, wbKey, wsName)?.let {
            buildAnnotatedString {
                append("=")
                append(it)
            }
        }
    }

    override fun toProto(): CellContentProto {
        return CellContentProto.newBuilder()
            .setCellValue(this.cellValue.toProto())
            .setFormula(this.fullFormula)
            .build()
    }

    companion object {
        fun randomNumericContent():CellContentImp{
            val num = (1 .. 1000).random()
            return CellContentImp(
                cellValueMs = ms(num.toCellValue()),
                exUnit = null,
                originalText = num.toString()
            )
        }
        fun randomExUnitContent():CellContentImp{
            val num=(1 .. 1000).random()
            return CellContentImp(
                cellValueMs = ms(CellValue.empty),
                exUnit = IntUnit(num),
                originalText = "=${num}"
            )
        }
        val empty = CellContentImp(originalText=null)

        /**
         * create a CellContent from a translation Rs
         */
        fun fromTransRs(rs: Rs<ExUnit, ErrorReport>,originalText:String?=null): CellContentImp {
            when (rs) {
                is Ok -> return CellContentImp(
                    exUnit = rs.value,
                    originalText=originalText
                )
                is Err -> return CellContentImp(
                    cellValueMs = CellValue.fromTransError(rs.error).toMs(),
                    originalText=originalText
                )
            }
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): CellContent {
        if (exUnit != null) {
            return this.copy(exUnit = exUnit.shift(oldAnchorCell, newAnchorCell))
        } else {
            return this
        }
    }

    /**
     * re-run the ExUnit if possible and refresh the [cellValueMs] obj
     */
    override val cellValueAfterRun: CellValue
        get() {
            if (exUnit != null) {
                try {
                    val exUnitRs = exUnit.runRs()
                    val cv: CellValue = CellValue.fromRs(exUnitRs)
                    internalSetCellValue(cv)
                } catch (e: Throwable) {
                    when (e) {
                        is StackOverflowError -> {
                            internalSetCellValue(
                                CellValue.from(
                                    CellErrors.OverflowError.report()
                                )
                            )
                        }
                        else -> {
                            internalSetCellValue(
                                CellValue.from(
                                    CommonErrors.ExceptionError.report(e)
                                )
                            )
                        }
                    }
                }
            }
            val rt= cellValue
            return rt
        }
    override val cellValue: CellValue by this.cellValueMs
    override fun toDm(): CellContentDM {
        return CellContentDM(
            cellValue = this.cellValue,
            formula = this.fullFormula
        )
    }

    override fun reRunRs(): Rse<CellContent> {
        if (this.exUnit == null) {
            return Ok(this)
        } else {
            val exUnitRs = exUnit.runRs()
            val newCellValue = CellValue.fromRs(exUnitRs)
            val rt = this.setCellValue(newCellValue)
            return Ok(rt)
        }
    }

    override fun reRun(): CellContent? {
        return reRunRs().component1()
    }

    override val editableStr: String
        get() {
            if (this.isFormula) {
                return this.fullFormula ?: ""
            } else {
                return this.cellValueAfterRun.editableValue ?: ""
            }
        }

    override fun isEmpty(): Boolean {
        return fullFormula == null && cellValueAfterRun.isEmpty()
    }

    override val displayText: String
        get() {
            return cellValue.displayText
        }

    override val isFormula: Boolean
        get() {
            val f = fullFormula
            return f != null && f.isNotEmpty()
        }

    private fun internalSetCellValue(cv:CellValue){
        cellValueMs.value = cv
    }

    override fun setValueAndDeleteExUnit(cv: CellValue): CellContent {
        internalSetCellValue(cv)
        return this.copy(exUnit = null)
    }

    override fun setCellValue(cv: CellValue): CellContent {
        internalSetCellValue(cv)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is CellContent) {
            val c1 = cellValue == other.cellValue
            val c2 = exUnit == other.exUnit
            return c1 && c2
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        var result = fullFormula?.hashCode() ?: 0
        result = 31 * result + cellValue.hashCode()
        return result
    }
}
