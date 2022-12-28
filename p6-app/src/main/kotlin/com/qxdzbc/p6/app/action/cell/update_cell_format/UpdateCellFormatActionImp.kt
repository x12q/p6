package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.format2.CellFormatTable
import com.qxdzbc.p6.ui.format2.FormatTable
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(scope = P6AnvilScope::class)
class UpdateCellFormatActionImp @Inject constructor(
    private val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
) : UpdateCellFormatAction {

    private val sc by stateContainerSt

    private fun <T> runSetFormat(
        formatValue: T?,
        cellId: CellId,
        fmtMs: Ms<CellFormatTable>,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ) {
        val newTable = getFormatTable(fmtMs.value)
            .removeValue(cellId.address)
            .let {
                if (formatValue != null) {
                    it.addValue(cellId.address, formatValue)
                } else {
                    it
                }
            }
        fmtMs.value = updateCellFormatTable(fmtMs.value, newTable)
    }

    fun <T> makeCommandForFormatOneCell(
        cellId: CellId,
        formatValue: T?,
        fmtMs: Ms<CellFormatTable>,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ): Command {
        val oldFormatValue = getFormatTable(fmtMs.value).getFirstValue(cellId.address)
        val command = Commands.makeCommand(
            run = {
                runSetFormat(
                    formatValue = formatValue,
                    cellId = cellId,
                    fmtMs = fmtMs,
                    getFormatTable = getFormatTable,
                    updateCellFormatTable = updateCellFormatTable
                )
            },
            undo = {
                runSetFormat(
                    formatValue = oldFormatValue,
                    cellId = cellId,
                    fmtMs = fmtMs,
                    getFormatTable = getFormatTable,
                    updateCellFormatTable = updateCellFormatTable
                )
            }
        )
        return command
    }

    fun <T> updateFormatOnOneCell(
        cellId: CellId,
        formatValue: T?,
        undo: Boolean,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ) {
        sc.getCellFormatTable2Ms(cellId)?.also { fmtMs ->
            if (undo) {
                sc.getWbState(cellId.wbKeySt)?.also { wbState ->
                    wbState.commandStackMs.value = wbState.commandStack.add(
                        makeCommandForFormatOneCell(cellId, formatValue, fmtMs, getFormatTable, updateCellFormatTable)
                    )
                }
            }
            runSetFormat(formatValue, cellId, fmtMs, getFormatTable, updateCellFormatTable)
        }
    }

    /**
     * Make an undoable command for the action of formatting cells selected by a cursor
     */
    fun <T> makeCommandForFormattingOnSelectedCells(
        formatValue: T?,
        cursorState: CursorState,
        cellFormatTableMs: Ms<CellFormatTable>,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ): Command {
        val command: Command = object : BaseCommand() {
            val _formatValue = formatValue
            val _cursorState = cursorState
            val _cellFormatTableMs = cellFormatTableMs
            val oldFormatConfig1 =
                getFormatTable(_cellFormatTableMs.value).getMultiValueFromRangesIncludeNullFormat(_cursorState.allRanges)
            val oldFormatConfig2 =
                getFormatTable(_cellFormatTableMs.value).getMultiValueFromCellsIncludeNullFormat(_cursorState.allFragCells)

            override fun run() {
                _cellFormatTableMs.value = updateCellFormatTableWithNewFormatValueOnSelectedCells(
                    formatValue = _formatValue,
                    ranges = _cursorState.allRanges,
                    cells = _cursorState.allFragCells,
                    currentFormatTable = _cellFormatTableMs.value,
                    getFormatTable = getFormatTable,
                    updateCellFormatTable = updateCellFormatTable
                )
            }

            override fun undo() {
                val newCellFormatTable =
                    (oldFormatConfig1 + oldFormatConfig2).fold(cellFormatTableMs.value) { acc, (rangeSet, t) ->
                        updateCellFormatTableWithNewFormatValueOnSelectedCells(
                            formatValue = t,
                            ranges = rangeSet.ranges,
                            cells = emptyList(),
                            currentFormatTable = acc,
                            getFormatTable = getFormatTable,
                            updateCellFormatTable = updateCellFormatTable
                        )
                    }
                cellFormatTableMs.value = newCellFormatTable
            }
        }
        return command
    }

    fun <T> updateFormatOfSelectedCells(
        formatValue: T?,
        undo: Boolean,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ) {
        sc.getActiveCursorState()?.also { csst ->
            sc.getCellFormatTable2Ms(csst)?.also { fmtMs ->
                if (undo) {
                    sc.getWbState(csst.wbKeySt)?.also {
                        val command = makeCommandForFormattingOnSelectedCells(
                            formatValue = formatValue,
                            cursorState = csst,
                            cellFormatTableMs = fmtMs,
                            getFormatTable = getFormatTable,
                            updateCellFormatTable = updateCellFormatTable
                        )
                        it.commandStackMs.value = it.commandStack.add(command)
                    }
                }
                fmtMs.value = updateCellFormatTableWithNewFormatValueOnSelectedCells(
                    formatValue = formatValue,
                    ranges = csst.allRanges,
                    cells = csst.allFragCells,
                    currentFormatTable = fmtMs.value,
                    getFormatTable = getFormatTable,
                    updateCellFormatTable = updateCellFormatTable
                )
            }
        }
    }

    fun <T> updateCellFormatTableWithNewFormatValueOnSelectedCells(
        formatValue: T?,
        ranges: Collection<RangeAddress>,
        cells: Collection<CellAddress>,
        currentFormatTable: CellFormatTable,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ): CellFormatTable {
        val newTable = getFormatTable(currentFormatTable)
            .removeValueFromMultiRanges(ranges)
            .removeValueFromMultiCells(cells)
            .let {
                if (formatValue != null) {
                    it.addValueForMultiRanges(ranges, formatValue)
                        .addValueForMultiCells(cells, formatValue)
                } else {
                    it
                }
            }
        return updateCellFormatTable(currentFormatTable, newTable)
    }


    override fun setCellBackgroundColor(cellId: CellId, color: Color?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = color,
            undo = undo,
            getFormatTable = CellFormatTable::cellBackgroundColorTable,
            updateCellFormatTable = CellFormatTable::setCellBackgroundColorTable
        )
    }

    override fun setBackgroundColorOnSelectedCells(color: Color?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = color,
            undo = undo,
            getFormatTable = CellFormatTable::cellBackgroundColorTable,
            updateCellFormatTable = CellFormatTable::setCellBackgroundColorTable
        )
    }

    override fun setCellTextSize(cellId: CellId, textSize: Float?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            undo = undo,
            formatValue = textSize,
            getFormatTable = CellFormatTable::textSizeTable,
            updateCellFormatTable = CellFormatTable::setTextSizeTable
        )
    }

    override fun setSelectedCellsTextSize(textSize: Float?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = textSize,
            undo = undo,
            getFormatTable = CellFormatTable::textSizeTable,
            updateCellFormatTable = CellFormatTable::setTextSizeTable
        )
    }

    override fun setCellTextColor(cellId: CellId, color: Color?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            undo = undo,
            formatValue = color,
            getFormatTable = CellFormatTable::textColorTable,
            updateCellFormatTable = CellFormatTable::setTextColorTable
        )
    }

    override fun setTextColorOnSelectedCells(color: Color?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = color,
            undo = undo,
            getFormatTable = CellFormatTable::textColorTable,
            updateCellFormatTable = CellFormatTable::setTextColorTable
        )
    }

    override fun setCellTextUnderlined(cellId: CellId, underlined: Boolean?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = underlined,
            undo = undo,
            getFormatTable = CellFormatTable::textUnderlinedTable,
            updateCellFormatTable = CellFormatTable::setTextUnderlinedTable
        )
    }

    override fun setUnderlinedOnSelectedCells(underlined: Boolean?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = underlined,
            undo = undo,
            getFormatTable = CellFormatTable::textUnderlinedTable,
            updateCellFormatTable = CellFormatTable::setTextUnderlinedTable
        )
    }

    override fun setCellTextCrossed(cellId: CellId, crossed: Boolean?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = crossed,
            undo = undo,
            getFormatTable = CellFormatTable::textCrossedTable,
            updateCellFormatTable = CellFormatTable::setTextCrossedTable
        )
    }

    override fun setCrossedOnSelectedCell(cellId: CellId, crossed: Boolean?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = crossed,
            undo = undo,
            getFormatTable = CellFormatTable::textCrossedTable,
            updateCellFormatTable = CellFormatTable::setTextCrossedTable
        )
    }


    override fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = alignment,
            undo = undo,
            getFormatTable = CellFormatTable::textHorizontalAlignmentTable,
                    updateCellFormatTable = CellFormatTable::setTextHorizontalAlignmentTable
        )
    }

    override fun setHorizontalAlignmentOnSelectedCells(
        cellId: CellId,
        alignment: TextHorizontalAlignment?,
        undo: Boolean
    ) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            undo = undo,
            getFormatTable = CellFormatTable::textHorizontalAlignmentTable,
            updateCellFormatTable = CellFormatTable::setTextHorizontalAlignmentTable
        )
    }

    override fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?, undo: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = alignment,
            undo = undo,
            getFormatTable = CellFormatTable::textVerticalAlignmentTable,
                    updateCellFormatTable = CellFormatTable::setTextVerticalAlignmentTable
        )
    }

    override fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            undo = undo,
            getFormatTable = CellFormatTable::textVerticalAlignmentTable,
            updateCellFormatTable = CellFormatTable::setTextVerticalAlignmentTable
        )
    }
}
