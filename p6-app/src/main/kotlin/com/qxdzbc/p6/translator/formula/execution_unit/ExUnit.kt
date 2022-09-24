package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorProvider

/**
 * An ExUnit (execution unit) is an obj representing a formula, when it is run it will return something that can be stored by a Cell.
 */
interface ExUnit : Shiftable, ColorKey {
    /**
     * get a list of range + cell ExUnit (if any) from this unit
     */
    fun getCellRangeExUnit(): List<ExUnit> {
        return emptyList()
    }
    /**
     * get a list of range ExUnit (if any) from this unit
     */
    fun getRanges(): List<RangeAddress> {
        return emptyList()
    }

    /**
     * get a list of range id ExUnit (if any) from this unit
     */
    fun getRangeIds():List<RangeId> {
        return emptyList()
    }

    fun toColorFormula(
        colorProvider: ColorProvider,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString? {
        val color: Color? = colorProvider.getColor(this)
        val str: String? = toShortFormula(wbKey, wsName)
        val rt: AnnotatedString? = str?.let {
            buildAnnotatedString {
                if (color != null) {
                    withStyle(style = SpanStyle(color = color)) {
                        append(it)
                    }
                } else {
                    append(it)
                }
            }
        }
        return rt
    }

    /**
     * Perform a shift on all shift-able element (range, cell)
     */
    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit

    /**
     * convert exUnit to a formula
     */
    fun toFormula(
    ): String?

    fun toShortFormula(
        wbKey: WorkbookKey? = null,
        wsName: String? = null
    ): String?

    /**
     * when this run, it returns something
     */
    fun run(): Result<Any, ErrorReport>

    companion object {
        /**
         * extract value from a variable [r1], if it is a cell, return the value inside the cell, otherwise, return [r1] itself.
         * @param defaultValue: default value for when [r1] is a cell and empty
         */
        fun extractR(r1: Any, defaultValue: Any = 0): Any {
            val trueR1 = when (r1) {
                is Cell -> r1.valueAfterRun ?: defaultValue
                is Range -> {
                    if (r1.isCell) {
                        r1.cells[0].valueAfterRun ?: defaultValue
                    } else {
                        r1
                    }
                }
                else -> r1
            }
            return trueR1
        }
    }
}

