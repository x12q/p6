package com.qxdzbc.p6.app.document.worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.document.cell.*
import com.qxdzbc.p6.app.document.cell.CellImp.Companion.toModel
import com.qxdzbc.p6.app.document.cell.CellImp.Companion.toShallowModel
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.OneOffRange
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetIdImp

data class WorksheetImp(
    override val idMs: Ms<WorksheetId>,
    override val table: TableCR<Int, Int, Ms<Cell>> = emptyTable,
    override val rangeConstraint: RangeConstraint = P6R.worksheetValue.defaultRangeConstraint,
) : BaseWorksheet() {


    companion object {
        val emptyTable: TableCR<Int, Int, Ms<Cell>> = ImmutableTableCR()
        fun fromCellList(
            name: String,
            cellList: List<Ms<Cell>> = emptyList(),
            rangeConstraint: RangeConstraint = P6R.worksheetValue.defaultRangeConstraint,
            wbKeyMs: Ms<WorkbookKey>,
        ): WorksheetImp {

            val cellMap =  cellList
                .groupBy { it.value.address.colIndex }
                .map { (colIndex, cl) -> colIndex to cl.associateBy { it.value.address.rowIndex } }
                .toMap()
            val rt=WorksheetImp(
                nameMs = ms(name),
                table = ImmutableTableCR(cellMap),
                rangeConstraint = rangeConstraint,
                wbKeySt = wbKeyMs
            )
            return rt
        }

        /**
         * Create a shallow model from a proto. A shallow model is one that contain fake Ms or St states that do not exist in the app state. Be extra careful when using them.
         */
        fun WorksheetProto.toShallowModel(wbKeyMs: Ms<WorkbookKey>, translator: P6Translator<ExUnit>): Worksheet {
            var ws: Worksheet = WorksheetImp(
                nameMs = ms(this.name),
                table = ImmutableTableCR(),
                wbKeySt = wbKeyMs
            )
            for (cell: Cell in cellsList.map { it.toShallowModel(translator) }) {
                ws = ws.addOrOverwrite(cell)
            }
            return ws
        }
    }

    constructor(
        nameMs: Ms<String>,
        wbKeySt: St<WorkbookKey>,
        table: TableCR<Int, Int, Ms<Cell>> = emptyTable,
        rangeConstraint: RangeConstraint = P6R.worksheetValue.defaultRangeConstraint,
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

    override fun reRun(): Worksheet {
        forEachCell{cellMs, colIndex, rowIndex ->
                val newCell = cellMs.value.reRun()
                if (newCell != null) {
                    cellMs.value = newCell
                }
        }
        return this
    }

    private fun forEachCell(f:(cellMs:Ms<Cell>,colIndex:Int,rowIndex:Int)->Unit){
        val cellMap = table.dataMap
        for ((colIndex, col) in cellMap) {
            for ((rowIndex, cellMs) in col) {
                f(cellMs,colIndex, rowIndex)
            }
        }
    }

    override fun reRunAndRefreshDisplayText(): Worksheet {
        forEachCell{cellMs, colIndex, rowIndex ->
                val newCell = cellMs.value.reRun()?.evaluateDisplayText()
                if (newCell != null) {
                    cellMs.value = newCell
                }
        }
        return this
    }

    override fun refreshDisplayText():Worksheet{
        forEachCell{cellMs, colIndex, rowIndex ->
            val newCell = cellMs.value.evaluateDisplayText()
            cellMs.value = newCell
        }
        return this
    }

    override var id: WorksheetId by idMs

    override fun range(address: RangeAddress): Result<Range, ErrorReport> {
        if (this.rangeConstraint.contains(address)) {
            return Ok(
                OneOffRange(
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
        this.idMs.value = this.idMs.value.pointToWbKeySt(wbKeySt)
        return this
    }

    override val name: String get() = nameMs.value
    override val cellMsList: List<Ms<Cell>> get() = this.table.allElements
    override val rowRange: IntRange
        get() = usedRange.rowRange
    override val colRange: IntRange
        get() = usedRange.colRange

    override fun toProto(): WorksheetProto {
        return WorksheetProto.newBuilder()
            .setName(this.name)
            .addAllCells(this.cells.map { it.toProto() })
            .build()
    }

    override fun updateCellValue(cellAddress: CellAddress, value: Any?): Result<Worksheet, ErrorReport> {
        val cellRs = this.getCellOrDefaultRs(cellAddress)
        val rt = cellRs.map { cell ->
            val newContent = cell.content.setValueAndDeleteExUnit(CellValue.fromAny(value))
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
        val address = cell.address
        val newCell = CellImp(
            id = CellId(address, wbKeySt, wsNameSt),
            content = cell.content
        )
        val cMs = this.getCellMs(address)
        if (cMs != null) {
            cMs.value = cell
            return this
        } else {
            val newTable = table.set(address, ms(newCell))
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
            var newWs: Worksheet = this.removeAllCell()
            for (cellProto: DocProtos.CellProto in wsProto.cellsList) {
                val newCell = cellProto.toModel(wbKeySt, wsNameSt, translator)
                val cMs: Ms<Cell> = this.getCellMs(newCell.address)?.apply {
                    value = newCell
                } ?: ms(newCell)
                newTable = newTable.set(newCell.address, cMs)
                newWs = newWs.addOrOverwrite(newCell)
            }
            return newWs.reRunAndRefreshDisplayText()
        } else {
            throw IllegalArgumentException("Cannot update sheet named \"${this.name}\" with data from sheet named \"${wsProto.name}\"")
        }
    }

    override fun removeAllCell(): Worksheet {
        return this.copy(table = table.removeAll())
    }

    override fun setWsName(newName: String): Worksheet {
        this.nameMs.value = newName
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is Worksheet) {
            val sameName = this.name == other.name
            val sameWb = this.wbKey == other.wbKey
            val sameCellMap = run {
                val cellMap1: Map<Int, Map<Int, Cell>> = this.table.dataMap.mapValues {
                    it.value.mapValues { it.value.value }
                }
                val cellMap2: Map<Int, Map<Int, Cell>> = other.table.dataMap.mapValues {
                    it.value.mapValues { it.value.value }
                }
                cellMap1 == cellMap2
            }
            val sameConstraint = this.rangeConstraint == other.rangeConstraint
            return listOf(sameName, sameWb, sameCellMap, sameConstraint).all { it }
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        var result = table.hashCode()
        result = 31 * result + rangeConstraint.hashCode()
        result = 31 * result + wbKey.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
