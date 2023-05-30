package com.qxdzbc.p6.translator.formula.execution_unit

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.common.utils.TypeUtils.checkStAndCast
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.range.Range

/**
 * This obj contains utils function for working with [ExUnit].
 */
object ExUnits {
    /**
     * extract value from a variable [r1], if it is a cell, return the value inside the cell, otherwise, return null
     */
    fun extractFromCellOrNull(r1: Any): Any? {
        return extractFromCellOrDefault(r1,null)
    }

    /**
     * extract value from a variable [r1], if it is a cell, return the value inside the cell, otherwise, return [r1] itself. If the value inside the cell is null, return the default value.
     *
     * @param defaultValue: default value for when [r1] is a cell and empty
     */
    private fun extractFromCellOrDefault(r1: Any, defaultValue: Any?): Any? {
         when (r1) {
            is Cell -> {
                val rt= r1.valueAfterRun ?: defaultValue
                return rt
            }
            is Range -> {
                if (r1.isCell) {
                    val rt=r1.cells[0].valueAfterRun ?: defaultValue
                    return rt
                } else {
                    return r1
                }
            }
            else -> {
                val casted:St<Cell>? = r1.checkStAndCast()
                if(casted!=null){
                    val rt= casted.value.valueAfterRun ?: defaultValue
                    return rt
                }else{
                    return r1
                }
            }
        }
    }
}
