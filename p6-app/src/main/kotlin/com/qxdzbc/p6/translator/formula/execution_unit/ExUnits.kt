package com.qxdzbc.p6.translator.formula.execution_unit

import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.Range

object ExUnits {
    /**
     * extract value from a variable [r1], if it is a cell, return the value inside the cell, otherwise, return null
     */
    fun extractFromCellOrNull(r1: Any): Any? {
        return extractFromCellOrDefaultOrNull(r1,null)
    }

    /**
     * extract value from a variable [r1], if it is a cell, return the value inside the cell, otherwise, return [r1] itself. If the cell-value is null, return the default value.
     *
     * @param defaultValue: default value for when [r1] is a cell and empty
     */
    fun extractFromCellOrDefaultOrNull(r1: Any, defaultValue: Any?): Any? {
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
