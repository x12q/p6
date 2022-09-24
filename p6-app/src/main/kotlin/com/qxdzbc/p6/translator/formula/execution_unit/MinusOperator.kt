package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider

/**
 * ExUnit for "-" operator
 */
data class MinusOperator(val u1: ExUnit, val u2: ExUnit) : ExUnit {
    override fun getRangeIds(): List<RangeId> {
        return u1.getRangeIds() + u2.getRangeIds()
    }
    override fun getCellRangeExUnit(): List<ExUnit> {
        return u1.getCellRangeExUnit() + u2.getCellRangeExUnit()
    }

    override fun getRanges(): List<RangeAddress> {
        return u1.getRanges() + u2.getRanges()
    }

    override fun toColorFormula(
        colorProvider: ColorProvider,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString? {
        val f1 = u1.toColorFormula(colorProvider, wbKey, wsName)
        val f2 = u2.toColorFormula(colorProvider, wbKey, wsName)
        if (f1 != null && f2 != null) {
            return buildAnnotatedString {
                append(f1)
                append("-")
                append(f2)
            }
        } else {
            return null
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this.copy(
            u1 = u1.shift(oldAnchorCell, newAnchorCell),
            u2 = u2.shift(oldAnchorCell, newAnchorCell)
        )
    }

    override fun toFormula(): String? {
        val f1 = u1.toFormula()
        val f2 = u2.toFormula()
        if (f1 != null && f2 != null) {
            return "${f1} - ${f2}"
        } else {
            return null
        }
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String? {
        val f1 = u1.toShortFormula(wbKey, wsName)
        val f2 = u2.toShortFormula(wbKey, wsName)
        if (f1 != null && f2 != null) {
            return "${f1} - ${f2}"
        } else {
            return null
        }
    }

    override fun runRs(): Result<Double, ErrorReport> {
        val r1Rs = u1.runRs()
        val rt = r1Rs.andThen { r1 ->
            val r2Rs = u2.runRs()
            r2Rs.andThen { r2 ->
                val trueR1 = ExUnits.extractFromCellOrNull(r1)?:0
                val trueR2 = ExUnits.extractFromCellOrNull(r2)?:0
                if (trueR1 is Number && trueR2 is Number) {
                    Ok(trueR1.toDouble() - (trueR2.toDouble()))
                } else {
                    ExUnitErrors.IncompatibleType.report(
                        "Expect two numbers, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
                    ).toErr()
                }
            }
        }
        return rt
    }
}

