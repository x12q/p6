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
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

/**
 * ExUnit for unary "-"
 */
data class UnarySubtract(val u: ExUnit) : ExUnit {
    override fun getRangeIds(): List<RangeId> {
        return u.getRangeIds()
    }
    override fun getCellRangeExUnit(): List<ExUnit> {
        return u.getCellRangeExUnit()
    }

    override fun getRanges(): List<RangeAddress> {
        return u.getRanges()
    }

    override fun toColorFormula(
        colorMap: ColorMap,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString? {
        val f1 = u.toColorFormula(colorMap, wbKey, wsName)
        if (f1 != null) {
            return buildAnnotatedString {
                append("-")
                append(f1)
            }
        } else {
            return null
        }
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this.copy(u = u.shift(oldAnchorCell, newAnchorCell))
    }

    override fun toFormula(): String? {
        val f1 = u.toFormula()
        if (f1 != null) {
            return "-${f1}"
        } else {
            return null
        }
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String? {
        val f1 = u.toShortFormula(wbKey, wsName)
        if (f1 != null) {
            return "-${f1}"
        } else {
            return null
        }
    }

    override fun runRs(): Result<Double, ErrorReport> {
        val runRs = u.runRs()
        val rt = runRs.andThen { rs ->
            val trueR = ExUnits.extractFromCellOrNull(rs)?:0
            val negated = when (trueR) {
                is Int -> Ok(-trueR.toDouble())
                is Double -> Ok(-trueR)
                is Float -> Ok(-trueR.toDouble())
                else -> ExUnitErrors.IncompatibleType.report("Expect a number, but got ${trueR::class.simpleName}")
                    .toErr()
            }
            negated
        }
        return rt
    }
}

