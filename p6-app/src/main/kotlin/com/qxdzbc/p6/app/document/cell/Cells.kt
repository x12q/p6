package com.qxdzbc.p6.app.document.cell

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.app.document.cell.CellValue.Companion.toCellValue

object Cells {
    fun emptyIndCell(colIndex: Int, rowIndex: Int): Cell {
        return emptyIndCell(CellAddresses.fromIndices(colIndex, rowIndex))
    }

    fun emptyIndCell(label: String): Cell {
        return emptyIndCell(CellAddresses.fromLabel(label))
    }

    fun number(label: String, number: Number): Cell {
        val rt=emptyIndCell(label).setCellValue(number.toCellValue())
        return rt
    }

    fun str(label: String, str: String): Cell {
        return emptyIndCell(label).setCellValue(str.toCellValue())
    }

    fun emptyIndCell(cellAddress: CellAddress): Cell {
        return IndCellImp(
            cellAddress,
            CellContentImp.empty
        )
    }
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
                        return o.component2() is SingleErrorReport
                    }
                }
            }
            else -> return false
        }
    }
}
