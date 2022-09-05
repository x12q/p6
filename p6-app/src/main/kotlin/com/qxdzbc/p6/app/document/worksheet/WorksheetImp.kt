package com.qxdzbc.p6.app.document.worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.*
import com.qxdzbc.p6.app.document.cell.d.CellImp.Companion.toModel
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.RangeImp
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.common.R
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.worksheet.state.*
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map

data class WorksheetImp(
    override val idMs: Ms<WorksheetId>,
    override val table: TableCR<Int, Int, Ms<Cell>> = emptyTable,
    override val rangeConstraint: RangeConstraint = R.worksheetValue.defaultRangeConstraint,
) : Worksheet {

    companion object {
        val emptyTable: TableCR<Int, Int, Ms<Cell>> = ImmutableTableCR()
        fun fromCellList(
            name: String,
            cellList: List<Ms<Cell>> = emptyList(),
            rangeConstraint: RangeConstraint = R.worksheetValue.defaultRangeConstraint,
            wbKeyMs: Ms<WorkbookKey>,
        ): WorksheetImp {
            return WorksheetImp(
                nameMs = ms(name),
                table = ImmutableTableCR(cellList.groupBy { it.value.address.colIndex }
                    .map { (colIndex, cl) -> colIndex to cl.associateBy { it.value.address.rowIndex } }.toMap()),
                rangeConstraint = rangeConstraint,
                wbKeySt = wbKeyMs
            )
        }
    }

    constructor(
        nameMs: Ms<String>,
        wbKeySt: St<WorkbookKey>,
        table: TableCR<Int, Int, Ms<Cell>> = emptyTable,
        rangeConstraint: RangeConstraint = R.worksheetValue.defaultRangeConstraint,
    ) : this(
        idMs = ms(
            WorksheetIdImp(
                wsNameMs = nameMs,
                wbKeySt = wbKeySt,
            )
        ),
        table = table,
        rangeConstraint = rangeConstraint
    )

    override val nameMs: Ms<String> get() = idMs.value.wsNameMs
    override val wbKeySt: St<WorkbookKey> get() = idMs.value.wbKeySt
    override val wsNameSt: St<String>
        get() = id.wsNameMs

    override fun equals(other: Any?): Boolean {
        if (other is Worksheet) {
            val c1 = this.name == other.name
            val c2 = this.wbKey == other.wbKey


            val t1 = this.table.dataMap.mapValues {
                it.value.mapValues { it.value.value }
            }
            val t2 = other.table.dataMap.mapValues {
                it.value.mapValues { it.value.value }
            }
            val c3 = t1 == t2
            val c4 = this.rangeConstraint == other.rangeConstraint
            return listOf(c1, c2, c3, c4).all { it }
        } else {
            return false
        }
    }

    override fun reRun(): Worksheet {
        val cellMap = table.dataMap
        for ((colIndex, col) in cellMap) {
            for ((rowIndex, cell) in col) {
                cell.value.reRun()
            }
        }
        return this
    }

    override var id: WorksheetId by idMs

    override fun range(address: RangeAddress): Result<Range, ErrorReport> {
        if (this.rangeConstraint.contains(address)) {
            return Ok(
                RangeImp(
                    worksheet = this,
                    wbKeySt = wbKeySt,
                    address = address
                )
            )
        } else {
            return WorksheetErrors.InvalidRange.report(address, this.rangeConstraint).toErr()
        }
    }

    override val wbKey: WorkbookKey
        get() = wbKeySt.value


    override fun setWbKeySt(wbKeySt: St<WorkbookKey>): Worksheet {
        this.idMs.value =this.idMs.value.pointToWbKeySt(wbKeySt)
        return this
    }

    override val name: String get() = nameMs.value
    override val cellMsList: List<Ms<Cell>> get() = this.table.allElements

    override fun toProto(): WorksheetProto {
        return WorksheetProto.newBuilder()
            .setName(this.name)
            .addAllCell(this.cells.map { it.toProto() })
            .build()
    }

    override fun updateCellValue(cellAddress: CellAddress, value: Any?): Result<Worksheet, ErrorReport> {
        val cellRs = this.getCellOrDefaultRs(cellAddress)
        val rt = cellRs.map { cell ->
            val newContent = cell.content.setValue(CellValue.fromAny(value))
            val newCell = cell.setContent(newContent)
            this.addOrOverwrite(newCell)
        }
        return rt
    }

    override fun updateCellContentRs(
        cellAddress: CellAddress,
        cellContent: CellContent
    ): Result<Worksheet, ErrorReport> {
        val cellRs = this.getCellOrDefaultRs(cellAddress)
        val rt = cellRs.map { cell ->
            val newCell = cell.setContent(cellContent)
            this.addOrOverwrite(newCell)
        }
        return rt
    }

    private val minCol: Int
    private val maxCol: Int

    private val minRow: Int
    private val maxRow: Int
    override val usedRange: RangeAddress

    init {
        if (cells.isNotEmpty()) {
            val cols = cells.map { it.address.colIndex }
            minCol = cols.minOf { it }
            maxCol = cols.maxOf { it }
            val rows = cells.map { it.address.rowIndex }
            minRow = rows.minOf { it }
            maxRow = rows.maxOf { it }
            usedRange = RangeAddress(
                colRange = minCol..maxCol,
                rowRange = minRow..maxRow
            )
        } else {
            minCol = 0
            maxCol = 0
            minRow = 0
            maxRow = 0
            usedRange = RangeAddresses.InvalidRange
        }
    }

    override fun getCellMsInRange(rangeAddress: RangeAddress): List<Ms<Cell>> {
        if (rangeAddress.contains(usedRange)) {
            return this.cellMsList
        } else {
            val rt = mutableListOf<Ms<Cell>>()
            val intersectionaRange = this.usedRange.intersect(rangeAddress)
            if (intersectionaRange != null && intersectionaRange.isValid()) {
                val iterator = intersectionaRange.cellIterator
                while (iterator.hasNext()) {
                    val address = iterator.next()
                    val c = this.table.getElement(address.colIndex, address.rowIndex)
                    if (c != null) {
                        rt.add(c)
                    }
                }
            }
            return rt
        }
    }

    override fun addOrOverwrite(cell: Cell): Worksheet {
        val cMs = this.getCellMsOrNull(cell.address)
        if (cMs != null) {
            cMs.value = cell
            return this
        } else {
            val newTable = table.set(cell.address, ms(cell))
            return this.copy(table = newTable)
        }
    }

    override fun removeCol(colIndex: Int): Worksheet {
        val newTable = table.removeCol(colIndex)
        return this.copy(table = newTable)
    }

    override fun removeRow(rowIndex: Int): Worksheet {
        val newTable = table.removeCol(rowIndex)
        return this.copy(table = newTable)
    }

    override fun removeCell(colKey: Int, rowKey: Int): Worksheet {
        val newTable = table.remove(colKey, rowKey)
        return this.copy(table = newTable)
    }

    override fun removeCells(cells: Collection<CellAddress>): Worksheet {
        val newTable = cells.fold(table) { accTable, cell ->
            accTable.remove(cell.colIndex, cell.rowIndex)
        }
        return this.copy(table = newTable)
    }

    override fun withNewData(wsProto: WorksheetProto, translator: P6Translator<ExUnit>): Worksheet {
        if (this.name == wsProto.name) {
            var newTable = ImmutableTableCR<Int, Int, Ms<Cell>>()
            for (cell: DocProtos.CellProto in wsProto.cellList) {
                val newDCell = cell.toModel(translator)
                val cMs = this.getCellMsOrNull(newDCell.address)?.apply {
                    value = newDCell
                } ?: ms(newDCell)
                newTable = newTable.set(newDCell.address, cMs)
            }
            return this.copy(table = newTable)
        } else {
            throw IllegalArgumentException("Cannot update sheet named \"${this.name}\" with data from sheet named \"${wsProto.name}\"")
        }
    }

    override fun setWsName(newName: String, translator: P6Translator<ExUnit>): Worksheet {
        var newTable = emptyTable
        val cellMap = table.dataMap
        for ((colIndex, col) in cellMap) {
            for ((rowIndex, cellMs) in col) {
                val cell: Cell by cellMs
                val formula = cell.formula
                val newCell: Cell = if (formula != null) {
                    val transRs = translator.translate(formula)
                    CellImp(
                        address = cell.address,
                        content = CellContentImp.fromTransRs(transRs, formula)
                    )
                } else {
                    cell
                }
                cellMs.value = newCell
                newTable = newTable.set(colIndex, rowIndex, cellMs)
            }
        }
        this.nameMs.value = newName
        return this.copy(table = newTable)
    }

    override fun hashCode(): Int {
        var result = table.hashCode()
        result = 31 * result + rangeConstraint.hashCode()
        result = 31 * result + wbKey.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}


fun WorksheetProto.toModel(wbKeyMs: Ms<WorkbookKey>, translator: P6Translator<ExUnit>): Worksheet {
    var table = ImmutableTableCR<Int, Int, Ms<Cell>>()
    for (cell: Cell in cellList.map { it.toModel(translator) }) {
        table = table.set(cell.address, ms(cell))
    }
    return WorksheetImp(
        nameMs = ms(this.name),
        table = table,
        wbKeySt = wbKeyMs
    )
}
