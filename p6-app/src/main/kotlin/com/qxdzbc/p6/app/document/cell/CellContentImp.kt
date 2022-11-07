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
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.CellContentProto
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

/**
 * Content = Formula + value
 * A class that holds content (value and formula) of a cell.
 * This implementation hold a mutable [CellValue] instance([cellValueMs]), whenever [cellValueAfterRun] is access, a new instance of cell value is computed. This is for auto formula computation
 */
data class CellContentImp(
    private val cellValueMs: Ms<CellValue> = ms(CellValue.empty),
    override val exUnit: ExUnit? = null,
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

    override fun equals(other: Any?): Boolean {
        if (other is CellContent) {
            val c1 = cellValue == other.cellValue
            val c2 = fullFormula == other.fullFormula
            return c1 && c2
        } else {
            return false
        }
    }

    companion object {
        val empty = CellContentImp()

        /**
         * create a CellContent from a translation Rs
         */
        fun fromTransRs(rs: Rs<ExUnit, ErrorReport>): CellContentImp {
            when (rs) {
                is Ok -> return CellContentImp(
                    exUnit = rs.value
                )
                is Err -> return CellContentImp(
                    cellValueMs = CellValue.fromTransError(rs.error).toMs(),
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

    override val cellValueAfterRun: CellValue
        get() {
            if (exUnit != null) {
                try {
                    val cv: CellValue = CellValue.fromRs(exUnit.runRs())
                    cellValueMs.value = cv
                } catch (e: Throwable) {
                    when (e) {
                        is StackOverflowError -> {
                            cellValueMs.value = CellValue.from(
                                CellErrors.OverflowError.report()
                            )
                        }
                        else -> {
                            cellValueMs.value = CellValue.from(
                                CommonErrors.ExceptionError.report(e)
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
//            cellValueMs.value = CellValue.fromRs(exUnitRs)
            val newCellValue = CellValue.fromRs(exUnitRs)
            val rt = this.setValue(newCellValue)
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

    override val displayStr: String
        get() {
            return cellValue.displayStr
        }

    override val isFormula: Boolean
        get() {
            val f = fullFormula
            return f != null && f.isNotEmpty()
        }

    override fun setValue(cv: CellValue): CellContent {
        cellValueMs.value = cv
        return this.copy(exUnit = null)
    }

    override fun hashCode(): Int {
        var result = fullFormula?.hashCode() ?: 0
        result = 31 * result + cellValue.hashCode()
        return result
    }
}
