package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

object Cells {

    /**
     * Check if an obj can be stored in a cell.
     * A legal obj is of type Result<T, ErrorReport> in which T is:
     * - Number (Int, Double, Float): all stored as Double
     * - String
     * - Boolean
     * - Cell
     * - Range
     */
    fun isLegalCellType(o: Any?): Boolean {
        when (o) {
            is Result<*, *> -> {
                when (o) {
                    is Ok<*> -> {
                        when (o.component1()) {
                            is Int, is Double, is Float,
                            is String, is Boolean,// TODO add  Cell, Range
                            -> return true

                            else -> return false
                        }
                    }
                    is Err<*> -> {
                        return o.component2() is ErrorReport
                    }
                }
            }
            else -> return false
        }
    }
}
