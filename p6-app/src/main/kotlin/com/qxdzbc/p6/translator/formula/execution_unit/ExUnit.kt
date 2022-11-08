package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

/**
 * An ExUnit (execution unit) is an obj representing a formula, when it runs it will return something that can be stored by a Cell.
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
    fun getRangeIds(): List<RangeId> {
        return emptyList()
    }

    /**
     * convert this ExUnit back into a formula string in which each range/cell address is colored.
     */
    fun toColorFormula(
        colorMap: ColorMap,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString? {
        val color: Color? = colorMap.getColor(this)
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

    /**
     * convert exUnit to a short formula in which workbook key and worksheet name in range and cell address maybe obmitted if they are the same as the input [wbKey] and [wsName]
     */
    fun toShortFormula(
        wbKey: WorkbookKey? = null,
        wsName: String? = null
    ): String?

    /**
     * returns something that can be stored in a cell.
     */
    fun runRs(): Result<Any?, ErrorReport>
    fun run(): Any? {
        return runRs().component1()
    }
}

