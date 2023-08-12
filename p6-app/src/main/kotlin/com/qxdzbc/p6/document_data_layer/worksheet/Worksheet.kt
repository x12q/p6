package com.qxdzbc.p6.document_data_layer.worksheet

import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.common.table.TableCR
import com.qxdzbc.p6.common.table.TableCRColumn
import com.qxdzbc.p6.common.table.TableCRRow
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.cell.Cell
import com.qxdzbc.p6.document_data_layer.cell.CellContent
import com.qxdzbc.p6.document_data_layer.range.Range
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.worksheet.state.WorksheetId

interface Worksheet : WithSize, WbWsSt {
    /**
     * re-run all the cells but does not update the display text
     */
    fun reRun()

    fun reRunAndRefreshDisplayText()

    val idMs: Ms<WorksheetId>

    var id: WorksheetId

    val nameMs: Ms<String>

    val name: String

    fun setWbKeySt(wbKeySt: St<WorkbookKey>)

    val usedRange: RangeAddress

    val table: TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    val cells: List<com.qxdzbc.p6.document_data_layer.cell.Cell>

    val cellMsList: List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    val rangeConstraint: RangeConstraint

    val usedRowRange:IntRange

    val usedColRange:IntRange

    override val size: Int get() = table.itemCount

    fun toProto(): WorksheetProto

    /**
     * return a range derived from this worksheet
     */
    fun range(address: RangeAddress): Rse<Range>

    fun updateCellValue(cellAddress: CellAddress, value: Any?): Rse<Unit>

    fun updateCellContentRs(cellAddress: CellAddress, cellContent: com.qxdzbc.p6.document_data_layer.cell.CellContent): Rse<Unit>

    fun getCellsInRange(rangeAddress: RangeAddress): List<com.qxdzbc.p6.document_data_layer.cell.Cell>

    fun getCellMsInRange(rangeAddress: RangeAddress): List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    fun getCellMs(cellAddress: CellAddress): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>?

    fun getCellMs(colIndex: Int, rowIndex: Int): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>?

    fun getCellMs(label: String): Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>?

    /**
     * @return error when the requested address does not exist.
     * @return a cell state ms if it exists
     */
    fun getCellMsRs(cellAddress: CellAddress): Rse<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    fun getCell(cellAddress: CellAddress): com.qxdzbc.p6.document_data_layer.cell.Cell?

    fun getCell(colIndex: Int, rowIndex: Int): com.qxdzbc.p6.document_data_layer.cell.Cell?

    fun getCell(label: String): com.qxdzbc.p6.document_data_layer.cell.Cell?

    /**
     * @return a cell if it exists or create an empty cell if the input address is legal
     * @return an error is the input address is illegal (out of the bound limit of this worksheet)
     */
    fun getCellOrDefaultRs(cellAddress: CellAddress): Rse<com.qxdzbc.p6.document_data_layer.cell.Cell>

    fun addOrOverwrite(cell: com.qxdzbc.p6.document_data_layer.cell.Cell)

    fun getColMs(colIndex: Int): List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    fun getRowMs(rowIndex: Int): List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>

    val allColumns:List<TableCRColumn<Int, com.qxdzbc.p6.document_data_layer.cell.Cell>>

    val allRows:List<TableCRRow<Int, com.qxdzbc.p6.document_data_layer.cell.Cell>>

    fun getCol(colIndex: Int): List<com.qxdzbc.p6.document_data_layer.cell.Cell>

    fun getRow(rowIndex: Int): List<com.qxdzbc.p6.document_data_layer.cell.Cell>

    fun removeCol(colIndex: Int)

    fun removeRow(rowIndex: Int)

    fun removeCell(colKey: Int, rowKey: Int)

    fun removeCell(cellAddress: CellAddress)

    fun removeCell(label:String)

    fun removeAllCell()

    fun removeCells(cells: Collection<CellAddress>)

    fun setWsName(newName: String)

    fun withNewData(wsProto: WorksheetProto, translator: P6Translator<ExUnit>)

    fun refreshDisplayText()

}
