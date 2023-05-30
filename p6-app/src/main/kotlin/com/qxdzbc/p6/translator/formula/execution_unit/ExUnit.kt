package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.Shiftable
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorKey
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
     * get a list of range address (if any) from this unit
     */
    fun getRanges(): List<RangeAddress> {
        return emptyList()
    }

    /**
     * get a list of range id (if any) from this unit
     */
    fun getRangeIds(): List<RangeId> {
        return emptyList()
    }

    /**
     * Create an [AnnotatedString] represented a colored formula text.
     * A colored formula is a text in which cell, range, wb texts are highlighted in colors
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
     * Perform a shift on all shift-able elements (range, cell) in this ExUnit
     * using the vector defined by [oldAnchorCell] and [newAnchorCell]
     */
    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit

    /**
     * convert exUnit to a formula text
     */
    fun toFormula(): String?

    /**
     * convert exUnit to a short formula in which workbook key and worksheet name in range and cell address maybe omitted if they are the same as the input [wbKey] and [wsName]
     */
    fun toShortFormula(
        wbKey: WorkbookKey? = null,
        wsName: String? = null
    ): String?

    /**
     * Run this [ExUnit] and returns something that can be stored in a cell.
     */
    fun runRs(): Rse<Any?>

    /**
     * Run this [ExUnit]. Throw an exception if encounter an error.
     */
    fun run(): Any? {
        return runRs().component1()
    }
}

