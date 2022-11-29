package com.qxdzbc.p6.app.document.worksheet

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.table.TableCRColumn
import com.qxdzbc.p6.app.common.table.TableCRColumnImp
import com.qxdzbc.p6.app.common.table.TableCRRow
import com.qxdzbc.p6.app.common.table.TableCRRowImp
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.CellImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress

abstract class BaseWorksheet : Worksheet {

    override fun getCellsInRange(rangeAddress: RangeAddress): List<Cell> {
        return this.getCellMsInRange(rangeAddress).map { it.value }
    }

    override fun getCellMs(cellAddress: CellAddress): Ms<Cell>? {
        return table.getElement(cellAddress.colIndex, cellAddress.rowIndex)
    }

    override fun getCellMs(colIndex: Int, rowIndex: Int): Ms<Cell>? {
        return table.getElement(colIndex, rowIndex)
    }

    override fun getCellMs(label: String): Ms<Cell>? {
        return table.getElement(CellAddress(label))
    }

    /**
     * TODO this implementation must be fixed so that it return Rse<Ms<Cell>?>.
     * null is for when the obj does not exist, error report for when the requested address is illegal.
     */
    @Deprecated("don't use, faulty")
    override fun getCellMsRs(cellAddress: CellAddress): Rse<Ms<Cell>> {
        val cellMs: Ms<Cell>? = this.table.getElement(cellAddress)
        val rt: Rse<Ms<Cell>> = cellMs?.let {
            Ok(it)
        } ?: WorksheetErrors.InvalidCell.report(cellAddress).toErr()
        return rt
    }

    override fun getCell(cellAddress: CellAddress): Cell? {
        return getCellMs(cellAddress)?.value
    }

    override fun getCell(colIndex: Int, rowIndex: Int): Cell? {
        return getCellMs(colIndex, rowIndex)?.value
    }

    override fun getCell(label: String): Cell? {
        return getCellMs(CellAddress(label))?.value
    }

    override fun getCellOrDefaultRs(cellAddress: CellAddress): Result<Cell, ErrorReport> {
        if (rangeConstraint.contains(cellAddress)) {
            return Ok(getCell(cellAddress) ?: CellImp(CellId(cellAddress, wbKeySt, wsNameSt)))
        } else {
            return Err(WorksheetErrors.InvalidCell(cellAddress))
        }
    }

    override val allRows: List<TableCRRow<Int, Cell>>
        get() {
            return table.allRows.map { row->
                TableCRRowImp(row.rowIndex,row.elements.map{it.value})
            }
        }
    override val allColumns: List<TableCRColumn<Int, Cell>>
        get() {
            return table.allColumns.map { col->
                TableCRColumnImp(col.colIndex,col.elements.map{it.value})
            }
        }

    override fun getColMs(colIndex: Int): List<Ms<Cell>> {
        return table.getCol(colIndex)
    }

    override fun getRowMs(rowIndex: Int): List<Ms<Cell>> {
        return table.getRow(rowIndex)
    }

    override fun getCol(colIndex: Int): List<Cell> {
        return getColMs(colIndex).map { it.value }
    }

    override fun getRow(rowIndex: Int): List<Cell> {
        return getRowMs(rowIndex).map { it.value }
    }

    override fun removeCell(cellAddress: CellAddress): Worksheet {
        return this.removeCell(cellAddress.colIndex, cellAddress.rowIndex)
    }

    override fun removeCell(label: String): Worksheet {
        return this.removeCell(CellAddress(label))
    }
}
