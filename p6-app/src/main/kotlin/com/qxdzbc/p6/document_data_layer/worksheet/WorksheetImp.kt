package com.qxdzbc.p6.document_data_layer.worksheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.common.table.ImmutableTableCR
import com.qxdzbc.p6.common.table.TableCR
import com.qxdzbc.p6.document_data_layer.cell.*
import com.qxdzbc.p6.document_data_layer.cell.CellImp.Companion.toModel
import com.qxdzbc.p6.document_data_layer.cell.CellImp.Companion.toShallowModel
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.OneOffRange
import com.qxdzbc.p6.document_data_layer.range.Range
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.document_data_layer.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.worksheet.WorksheetConstants
import com.qxdzbc.p6.ui.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.worksheet.state.WorksheetId
import com.qxdzbc.p6.ui.worksheet.state.WorksheetIdImp
import java.util.*

data class WorksheetImp(
    override val idMs: Ms<WorksheetId>,
    val tableMs: Ms<TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>> = ms(emptyTable),
    override val rangeConstraint: RangeConstraint = WorksheetConstants.defaultRangeConstraint,
) : BaseWorksheet() {

    override val table: TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> by tableMs

    constructor(
        nameMs: Ms<String>,
        wbKeySt: St<WorkbookKey>,
        table: TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> = emptyTable,
        rangeConstraint: RangeConstraint = WorksheetConstants.defaultRangeConstraint,
    ) : this(
        idMs = ms(
            WorksheetIdImp(
                wsNameMs = nameMs,
                wbKeySt = wbKeySt,
            )
        ),
        tableMs = ms(table),
        rangeConstraint = rangeConstraint
    )

    override val nameMs: Ms<String> get() = idMs.value.wsNameMs
    override val wbKeySt: St<WorkbookKey> get() = idMs.value.wbKeySt
    override val wsNameSt: St<String>
        get() = id.wsNameMs

    override fun reRun() {
        forEachCell{cellMs, colIndex, rowIndex ->
                val newCell = cellMs.value.reRun()
                if (newCell != null) {
                    cellMs.value = newCell
                }
        }
    }

    private fun forEachCell(f:(cellMs:Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>, colIndex:Int, rowIndex:Int)->Unit){
        val cellMap = table.dataMap
        for ((colIndex, col) in cellMap) {
            for ((rowIndex, cellMs) in col) {
                f(cellMs,colIndex, rowIndex)
            }
        }
    }

    override fun reRunAndRefreshDisplayText() {
        forEachCell{cellMs, colIndex, rowIndex ->
                val newCell = cellMs.value.reRun()?.evaluateDisplayText()
                if (newCell != null) {
                    cellMs.value = newCell
                }
        }
    }

    override fun refreshDisplayText() {
        forEachCell{cellMs, colIndex, rowIndex ->
            val newCell = cellMs.value.evaluateDisplayText()
            cellMs.value = newCell
        }
    }

    override var id: WorksheetId by idMs

    override fun range(address: RangeAddress): Rse<Range> {
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


    override fun setWbKeySt(wbKeySt: St<WorkbookKey>) {
        this.idMs.value = this.idMs.value.pointToWbKeySt(wbKeySt)
    }

    override val name: String get() = nameMs.value
    override val cellMsList: List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> get() = this.table.allElements
    override val usedRowRange: IntRange
        get() = usedRange.rowRange
    override val usedColRange: IntRange
        get() = usedRange.colRange

    override fun toProto(): WorksheetProto {
        return WorksheetProto.newBuilder()
            .setName(this.name)
            .addAllCells(this.cells.map { it.toProto() })
            .build()
    }

    override fun updateCellValue(cellAddress: CellAddress, value: Any?): Rse<Unit> {
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
        cellContent: com.qxdzbc.p6.document_data_layer.cell.CellContent
    ): Rse<Unit> {
        val cellRs = this.getCellOrDefaultRs(cellAddress)
        val rt = cellRs.map { cell ->
            val newCell = cell.setContent(cellContent)
            this.addOrOverwrite(newCell)
        }
        return rt
    }

    init {
        if (cells.isNotEmpty()) {
//            val cols = cells.map { it.address.colIndex }
//            minCol = cols.minOf { it }
//            maxCol = cols.maxOf { it }
//            val rows = cells.map { it.address.rowIndex }
//            minRow = rows.minOf { it }
//            maxRow = rows.maxOf { it }
//            usedRange = RangeAddress(
//                colRange = minCol..maxCol,
//                rowRange = minRow..maxRow
//            )
        } else {
//            minCol = 0
//            maxCol = 0
//            minRow = 0
//            maxRow = 0
//            usedRange = RangeAddressUtils.InvalidRange
        }
    }

    override fun getCellMsInRange(rangeAddress: RangeAddress): List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> {
        if (rangeAddress.contains(usedRange)) {
            return this.cellMsList
        } else {
            val rt = mutableListOf<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>()
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

    override fun addOrOverwrite(cell: com.qxdzbc.p6.document_data_layer.cell.Cell) {
        val address = cell.address
        val newCell = CellImp(
            id = CellId(address, wbKeySt, wsNameSt),
            content = cell.content
        )
        val cellMs = this.getCellMs(address)
        if (cellMs != null) {
            cellMs.value = cell
        } else {
            val newTable = table.set(address, ms(newCell))
            tableMs.value = newTable
        }
    }

    override fun removeCol(colIndex: Int) {
        val newTable = table.removeCol(colIndex)
        tableMs.value = newTable
    }

    override fun removeRow(rowIndex: Int) {
        val newTable = table.removeCol(rowIndex)
        tableMs.value = newTable
    }

    override fun removeCell(colKey: Int, rowKey: Int) {
        val newTable = table.remove(colKey, rowKey)
        tableMs.value = newTable
    }

    override fun removeCells(cells: Collection<CellAddress>) {
        val newTable = cells.fold(table) { accTable, cell ->
            accTable.remove(cell.colIndex, cell.rowIndex)
        }
        tableMs.value = newTable
    }

    override fun withNewData(wsProto: WorksheetProto, translator: P6Translator<ExUnit>) {
        if (this.name == wsProto.name) {
            var newTable = ImmutableTableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>()
            this.removeAllCell()
            for (cellProto: DocProtos.CellProto in wsProto.cellsList) {
                val newCell = cellProto.toModel(wbKeySt, wsNameSt, translator)
                val cMs: Ms<com.qxdzbc.p6.document_data_layer.cell.Cell> = this.getCellMs(newCell.address)?.apply {
                    value = newCell
                } ?: ms(newCell)
                newTable = newTable.set(newCell.address, cMs)
                this.addOrOverwrite(newCell)
            }
            reRunAndRefreshDisplayText()
        } else {
            throw IllegalArgumentException("Cannot update sheet named \"${this.name}\" with data from sheet named \"${wsProto.name}\"")
        }
    }

    override fun removeAllCell() {
        tableMs.value = table.removeAll()
    }

    override fun setWsName(newName: String) {
        this.nameMs.value = newName
    }

    override fun equals(other: Any?): Boolean {
        if (other is Worksheet) {
            val sameName = this.name == other.name
            val sameWb = this.wbKey == other.wbKey
            val sameCellMap = run {
                val cellMap1: Map<Int, Map<Int, com.qxdzbc.p6.document_data_layer.cell.Cell>> = this.table.dataMap.mapValues {
                    it.value.mapValues { it.value.value }
                }
                val cellMap2: Map<Int, Map<Int, com.qxdzbc.p6.document_data_layer.cell.Cell>> = other.table.dataMap.mapValues {
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

    companion object {
            fun random():Worksheet{
                val rt= WorksheetImp(
                    nameMs= ms("Worksheet-"+ UUID.randomUUID().toString()),
                    wbKeySt = ms(WorkbookKey.random()),
                    table = run {
                        var tb= ImmutableTableCR<Int,Int,Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>>()
                        for(c in 1 .. 10){
                            for (r in 1 .. 10){
                                tb = tb.set(c,r, ms(com.qxdzbc.p6.document_data_layer.cell.Cell.random(CellAddress(c,r))))
                            }
                        }
                        tb
                    }
                )
                return rt
            }

        val emptyTable: TableCR<Int, Int, Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> = ImmutableTableCR()
        fun fromCellList(
            name: String,
            cellList: List<Ms<com.qxdzbc.p6.document_data_layer.cell.Cell>> = emptyList(),
            rangeConstraint: RangeConstraint = WorksheetConstants.defaultRangeConstraint,
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
            val ws: Worksheet = WorksheetImp(
                nameMs = ms(this.name),
                table = ImmutableTableCR(),
                wbKeySt = wbKeyMs
            )
            for (cell: com.qxdzbc.p6.document_data_layer.cell.Cell in cellsList.map { it.toShallowModel(translator) }) {
                ws.addOrOverwrite(cell)
            }
            return ws
        }
    }

}
