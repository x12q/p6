package com.qxdzbc.p6.ui.document.worksheet.state

import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.command.CommandStack
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.ui.document.cell.state.CellState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.format.CellFormatTable

abstract class BaseWorksheetState : WorksheetState {

    override fun toProto(): DocProtos.WorksheetProto {
        val proto1 = this.worksheet.toProto()
        val rt = DocProtos.WorksheetProto.newBuilder(proto1)
            .setCellFormatTable(this.cellFormatTable.toProto())
            .putAllColumnWidthMap(this.columnWidthMap.mapValues { it.value.value.toInt() } )
            .putAllRowHeightMap(this.rowHeightMap.mapValues { it.value.value.toInt() })
            .build()

        return rt
    }

    override val redoStack: CommandStack
        get() = redoStackMs.value

    override val undoStack: CommandStack
        get() = undoStackMs.value

    override val cellFormatTable: CellFormatTable
        get() = cellFormatTableMs.value

    override fun getRulerState(rulerType: RulerType): RulerState {
        return when(rulerType){
            RulerType.Row->this.rowRulerState
            RulerType.Col->this.colRulerState
        }
    }

    override fun getCellState(label: String): CellState? {
        return this.getCellState(CellAddress(label))
    }

    override fun refresh() {
        this.refreshCellState()
    }

    override fun getCellState(colIndex: Int, rowIndex: Int): CellState? {
        return this.getCellState(CellAddress(colIndex, rowIndex))
    }

    override fun getCellStateMs(colIndex: Int, rowIndex: Int): MutableState<CellState>? {
        return this.getCellStateMs(CellAddress(colIndex, rowIndex))
    }

    override fun getCellState(cellAddress: CellAddress): CellState? {
        return this.getCellStateMs(cellAddress)?.value
    }

    override fun getColumnWidthOrDefault(colIndex: Int): Dp {
        return this.getColumnWidth(colIndex) ?: defaultColWidth
    }

    override fun changeColWidth(colIndex: Int, sizeDiff: Dp) {
        val sd = sizeDiff
        if (sd != 0.dp) {
            val oldSize = this.getColumnWidth(colIndex) ?: defaultColWidth
            val newSize = oldSize + sd
            if (newSize == defaultColWidth) {
                this.restoreColumnWidthToDefault(colIndex)
            } else {
                this.addColumnWidth(colIndex, maxOf(oldSize + sd, 0.dp))
            }
        }
    }

    override fun changeRowHeight(rowIndex: Int, sizeDiff: Dp) {
        if (sizeDiff != 0.dp) {
            val wsState = this
            val oldSize = wsState.getRowHeight(rowIndex) ?: defaultRowHeight
            val newSize = oldSize + sizeDiff
            if (newSize == defaultRowHeight) {
                wsState.restoreRowHeightToDefault(rowIndex)
            } else {
                wsState.addRowHeight(rowIndex, maxOf(oldSize + sizeDiff, 0.dp))
            }
        }
    }

    override val columnWidthMap: Map<Int, Dp> get() = colRulerState.itemSizeMap
    override val rowHeightMap: Map<Int, Dp> get() = rowRulerState.itemSizeMap
    override val defaultColWidth: Dp get() = colRulerState.defaultItemSize
    override val defaultRowHeight: Dp get() = rowRulerState.defaultItemSize

    override fun getRowHeightOrDefault(rowIndex: Int): Dp {
        return this.getRowHeight(rowIndex) ?: defaultRowHeight
    }

    override fun getColumnWidth(colIndex: Int): Dp? {
        return this.columnWidthMap[colIndex]
    }

    override fun addColumnWidth(colIndex: Int, colWidth: Dp) {
        this.colRulerStateMs.value = this.colRulerState.setItemSize(colIndex,colWidth)
    }

    override fun restoreColumnWidthToDefault(colIndex: Int) {
        this.colRulerStateMs.value = this.colRulerState.removeItemSize(colIndex)
    }

    override fun getRowHeight(rowIndex: Int): Dp? {
        return this.rowHeightMap[rowIndex]
    }

    override fun addRowHeight(rowIndex: Int, rowHeight: Dp) {
        this.rowRulerStateMs.value = this.rowRulerState.setItemSize(rowIndex,  rowHeight)
    }

    override fun restoreRowHeightToDefault(rowIndex: Int) {
        rowRulerStateMs.value = rowRulerState.removeItemSize(rowIndex)
    }
}
