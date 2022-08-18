package com.emeraldblast.p6.app.document.cell.d

import androidx.compose.runtime.getValue
import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.common.compose.ms
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

/**
 * A class that holds content (value and formula) of a cell.
 * This implementation hold a mutable [CellValue] instance([cellValueMs]), whenever [cellValueAfterRun] is access, a new instance of cell value is computed. This is for auto formula computation
 */
data class CellContentImp(
    override val cellValueMs: Ms<CellValue> = ms(CellValue.empty),
    override val formula: String? = null,
    private val exUnit: ExUnit? = null,
) : CellContent {

    init {
        checkStateLegality()
    }

    override fun equals(other: Any?): Boolean {
        if (other is CellContent) {
            val c1 = currentCellValue == other.currentCellValue
            val c2 = formula == other.formula
            return c1 && c2
        } else {
            return false
        }
    }

    private fun checkStateLegality() {
        val c1 = (exUnit != null && formula.isNullOrEmpty())
        val c2 = exUnit == null && formula?.isNotEmpty() == true
        val formulaTranslatedOk = !this.cellValueMs.value.isTranslatorErr
        if (formulaTranslatedOk) {
            if (c1 || c2) {
                throw IllegalStateException(
                    "ExUnit and formula must either be null or not null at the same time if the formula is translated successfully.\n" +
                            "formula = ${formula}\n" +
                            "exUnit = ${if (exUnit == null) "null" else "not null"}\n" +
                            "trans error = ${currentCellValue.isTranslatorErr}"
                )
            }
        }
    }

    companion object {
        /**
         * create a CellContent from a translation Rs
         */
        fun fromTransRs(rs: Rs<ExUnit, ErrorReport>, formula: String): CellContentImp {
            when (rs) {
                is Ok -> return CellContentImp(
                    formula = formula,
                    exUnit = rs.value
                )
                is Err -> return CellContentImp(
                    cellValueMs = CellValue.fromTransError(rs.error).toMs(),
                    formula = formula,
                )
            }
        }
    }

    override val cellValueAfterRun: CellValue
        get() {
            if (exUnit != null) {
                try {
                    val cv: CellValue = CellValue.fromRs(exUnit.run())
                    cellValueMs.value = cv
                } catch (e: Throwable) {
                    cellValueMs.value = CellValue.from(
                        CommonErrors.ExceptionError.report(e)
                    )
                }
            }
            return currentCellValue
        }
    override val currentCellValue: CellValue by this.cellValueMs


    override fun reRun(): CellContent? {
        checkStateLegality()
        if (this.exUnit == null && this.formula == null) {
            return null
        }
        if (this.exUnit != null && this.formula != null) {
            cellValueMs.value = CellValue.fromRs(exUnit.run())
            return this
        }
        return null
    }

    override val editableContent: String
        get() {
            if (this.isFormula) {
                return this.formula ?: ""
            } else {
                return this.cellValueAfterRun.editableValue ?: ""
            }
        }

    override fun isEmpty(): Boolean {
        return formula == null && cellValueAfterRun.isEmpty()
    }

    override val displayValue: String
        get() {
            return cellValueAfterRun.displayStr
        }

    override val isFormula: Boolean get() = formula != null && formula.isNotEmpty()

    override fun setFormula(newFormula: String): CellContent {
        cellValueMs.value = CellValue.empty
        return this.copy(formula = newFormula)
    }

    override fun setValue(cv: CellValue): CellContent {
        cellValueMs.value = cv
        return this.copy(formula = null)
    }

    override fun hashCode(): Int {
        var result = formula?.hashCode() ?: 0
        result = 31 * result + currentCellValue.hashCode()
        return result
    }
}
