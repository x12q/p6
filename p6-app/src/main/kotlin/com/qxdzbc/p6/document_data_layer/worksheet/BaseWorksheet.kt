package com.qxdzbc.p6.document_data_layer.worksheet

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.p6.common.table.TableCRColumn
import com.qxdzbc.p6.common.table.TableCRColumnImp
import com.qxdzbc.p6.common.table.TableCRRow
import com.qxdzbc.p6.common.table.TableCRRowImp
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.cell.CellImp
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddressUtils

abstract class BaseWorksheet : Worksheet {

    override val cells: List<com.qxdzbc.p6.document_data_layer.cell.Cell> by derivedStateOf {
        cellMsList.map { it.value }
    }

    private val minCol: Int by derivedStateOf {
        val cols = cells.map { it.address.colIndex }
        cols.minOfOrNull { it } ?: 0
    }

    private val maxCol: Int by derivedStateOf {
        val cols = cells.map { it.address.colIndex }
        cols.maxOfOrNull { it } ?: 0
    }

    private val minRow: Int by derivedStateOf {
        val rows = cells.map { it.address.rowIndex }
        rows.minOfOrNull { it } ?: 0
    }

    private val maxRow: Int by derivedStateOf {
        val rows = cells.map { it.address.rowIndex }
        rows.maxOfOrNull { it }?:0
    }

    override val usedRange: RangeAddress by derivedStateOf {
        if(listOf(minCol,maxCol,minRow,maxRow).all { it>=0 }){
            RangeAddress(
                colRange = minCol..maxCol,
                rowRange = minRow..maxRow
            )
        }else{
            RangeAddressUtils.InvalidRange
        }
    }

    override fun getCellsInRange(rangeAddress: RangeAddress): List<com.qxdzbc.p6.document_data_layer.cell.Cell> {
        return this.getCellMsInRange(rangeAddress).map { it.value }
    }

    override fun getCellMs(cellAddress: CellAddress): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>? {
        return table.getElement(cellAddress.colIndex, cellAddress.rowIndex)
    }

    override fun getCellMs(colIndex: Int, rowIndex: Int): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>? {
        return table.getElement(colIndex, rowIndex)
    }

    override fun getCellMs(label: String): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>? {
        return table.getElement(CellAddress(label))
    }

    override fun getCellMsRs(cellAddress: CellAddress): Rse<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> {
        val cellMs: Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>? = this.table.getElement(cellAddress)
        val rt: Rse<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> = cellMs?.let {
            Ok(it)
        } ?: WorksheetErrors.InvalidCell.report(cellAddress).toErr()
        return rt
    }

    override fun getCell(cellAddress: CellAddress): com.qxdzbc.p6.document_data_layer.cell.Cell? {
        return getCellMs(cellAddress)?.value
    }

    override fun getCell(colIndex: Int, rowIndex: Int): com.qxdzbc.p6.document_data_layer.cell.Cell? {
        return getCellMs(colIndex, rowIndex)?.value
    }

    override fun getCell(label: String): com.qxdzbc.p6.document_data_layer.cell.Cell? {
        return getCellMs(CellAddress(label))?.value
    }

    override fun getCellOrDefaultRs(cellAddress: CellAddress): Result<com.qxdzbc.p6.document_data_layer.cell.Cell, SingleErrorReport> {
        if (rangeConstraint.contains(cellAddress)) {
            return Ok(getCell(cellAddress) ?: CellImp(CellId(cellAddress, wbKeySt, wsNameSt)))
        } else {
            return Err(WorksheetErrors.InvalidCell(cellAddress))
        }
    }

    override val allRows: List<TableCRRow<Int, com.qxdzbc.p6.document_data_layer.cell.Cell>>
        get() {
            return table.allRows.map { row ->
                TableCRRowImp(row.rowIndex, row.elements.map { it.value })
            }
        }
    override val allColumns: List<TableCRColumn<Int, com.qxdzbc.p6.document_data_layer.cell.Cell>>
        get() {
            return table.allColumns.map { col ->
                TableCRColumnImp(col.colIndex, col.elements.map { it.value })
            }
        }

    override fun getColMs(colIndex: Int): List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> {
        return table.getCol(colIndex)
    }

    override fun getRowMs(rowIndex: Int): List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> {
        return table.getRow(rowIndex)
    }

    override fun getCol(colIndex: Int): List<com.qxdzbc.p6.document_data_layer.cell.Cell> {
        return getColMs(colIndex).map { it.value }
    }

    override fun getRow(rowIndex: Int): List<com.qxdzbc.p6.document_data_layer.cell.Cell> {
        return getRowMs(rowIndex).map { it.value }
    }

    override fun removeCell(cellAddress: CellAddress) {
        this.removeCell(cellAddress.colIndex, cellAddress.rowIndex)
    }

    override fun removeCell(label: String) {
        this.removeCell(CellAddress(label))
    }
}
