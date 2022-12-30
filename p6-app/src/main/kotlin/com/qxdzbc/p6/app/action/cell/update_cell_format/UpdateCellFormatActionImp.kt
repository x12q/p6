package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
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
import com.qxdzbc.p6.ui.format2.FormatConfig
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
        cellFormatTableMs: Ms<CellFormatTable>,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ): Command {
        val oldFormatValue = getFormatTable(cellFormatTableMs.value).getFirstValue(cellId.address)
        val command = Commands.makeCommand(
            run = {
                runSetFormat(
                    formatValue = formatValue,
                    cellId = cellId,
                    fmtMs = cellFormatTableMs,
                    getFormatTable = getFormatTable,
                    updateCellFormatTable = updateCellFormatTable
                )
            },
            undo = {
                runSetFormat(
                    formatValue = oldFormatValue,
                    cellId = cellId,
                    fmtMs = cellFormatTableMs,
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
        sc.getCellFormatTableMs(cellId)?.also { fmtMs ->
            if (undo) {
                sc.getCommandStackMs(cellId)?.also {
                    val command =
                        makeCommandForFormatOneCell(cellId, formatValue, fmtMs, getFormatTable, updateCellFormatTable)
                    it.value = it.value.add(command)
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
                    (oldFormatConfig1.all + oldFormatConfig2.all).fold(cellFormatTableMs.value) { acc, (rangeSet, t) ->
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
            sc.getCellFormatTableMs(csst)?.also { fmtMs ->
                if (undo) {
                    sc.getCommandStackMs(csst)?.also {
                        val command = makeCommandForFormattingOnSelectedCells(
                            formatValue = formatValue,
                            cursorState = csst,
                            cellFormatTableMs = fmtMs,
                            getFormatTable = getFormatTable,
                            updateCellFormatTable = updateCellFormatTable
                        )
                        it.value = it.value.add(command)
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

    fun makeCommandToApplyFormatConfig(wbWsSt: WbWsSt, config: FormatConfig): Command {
        val rt = object : BaseCommand() {
            val _wbWsSt = wbWsSt
            val newConfig = config
            val oldConfig = sc.getCellFormatTable(wbWsSt)?.let {
                it.getFormatConfigIncludeNullForConfig_Respectively(config)
            }
            val formatTableMs = sc.getCellFormatTableMs(_wbWsSt)
            override fun run() {
                // apply new config
                formatTableMs?.also {
                    it.value = it.value.applyConfig(newConfig)
                }
            }

            override fun undo() {
                formatTableMs?.also {
                    if (oldConfig != null) {
                        // apply old config
                        it.value = it.value.applyConfig(oldConfig)
                    } else {
                        // remove format using the ranges in new config
                        it.value = it.value.removeFormatByConfig_Flat(newConfig)
                    }
                }
            }
        }
        return rt
    }


    override fun applyFormatConfig(wbWsSt: WbWsSt, config: FormatConfig, undoable: Boolean) {
        if (undoable) {
            val command = makeCommandToApplyFormatConfig(wbWsSt, config)
            sc.getCommandStackMs(wbWsSt)?.also {
                it.value = it.value.add(command)
            }
            command.run()
        } else {
            sc.getCellFormatTableMs(wbWsSt)?.also {
                it.value = it.value.applyConfig(config)
            }
        }
    }

    override fun applyFormatConfig(wbWs: WbWs, config: FormatConfig, undoable: Boolean) {
        sc.getWbWsSt(wbWs)?.also {
            applyFormatConfig(it, config, undoable)
        }
    }

    fun makeClearFormatCommand(wbWsSt: WbWsSt, config: FormatConfig):Command{
        val command = object : BaseCommand() {
            val oldConfig = sc.getCellFormatTable(wbWsSt)?.getFormatConfigIncludeNullForConfig_Flat(config)
            val newConfig = config
            override fun run() {
                sc.getCellFormatTableMs(wbWsSt)?.also {
                    it.value = it.value.removeFormatByConfig_Respectively(newConfig)
                }
            }

            override fun undo() {
                oldConfig?.also {
                    sc.getCellFormatTableMs(wbWsSt)?.also {
                        it.value = it.value.removeFormatByConfig_Respectively(oldConfig)
                    }
                }
            }
        }
        return command
    }

    override fun clearFormat(wbWsSt: WbWsSt, config: FormatConfig, undoable: Boolean) {
        if (undoable) {
            sc.getCommandStackMs(wbWsSt)?.also {
                it.value = it.value.add(makeClearFormatCommand(wbWsSt, config))
            }
        }
        sc.getCellFormatTableMs(wbWsSt)?.also {
            it.value = it.value.removeFormatByConfig_Respectively(config)
        }
    }

    override fun clearFormat(wbWs: WbWs, config: FormatConfig, undoable: Boolean) {
        sc.getWbWsSt(wbWs)?.also {
            clearFormat(it,config,undoable)
        }
    }

    override fun setCellBackgroundColor(cellId: CellId, color: Color?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = color,
            undo = undoable,
            getFormatTable = CellFormatTable::cellBackgroundColorTable,
            updateCellFormatTable = CellFormatTable::setCellBackgroundColorTable
        )
    }

    override fun setBackgroundColorOnSelectedCells(color: Color?, undoable: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = color,
            undo = undoable,
            getFormatTable = CellFormatTable::cellBackgroundColorTable,
            updateCellFormatTable = CellFormatTable::setCellBackgroundColorTable
        )
    }

    override fun setCellTextSize(cellId: CellId, textSize: Float?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            undo = undoable,
            formatValue = textSize,
            getFormatTable = CellFormatTable::textSizeTable,
            updateCellFormatTable = CellFormatTable::setTextSizeTable
        )
    }

    override fun setSelectedCellsTextSize(textSize: Float?, undoable: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = textSize,
            undo = undoable,
            getFormatTable = CellFormatTable::textSizeTable,
            updateCellFormatTable = CellFormatTable::setTextSizeTable
        )
    }

    override fun setCellTextColor(cellId: CellId, color: Color?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            undo = undoable,
            formatValue = color,
            getFormatTable = CellFormatTable::textColorTable,
            updateCellFormatTable = CellFormatTable::setTextColorTable
        )
    }

    override fun setTextColorOnSelectedCells(color: Color?, undoable: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = color,
            undo = undoable,
            getFormatTable = CellFormatTable::textColorTable,
            updateCellFormatTable = CellFormatTable::setTextColorTable
        )
    }

    override fun setCellTextUnderlined(cellId: CellId, underlined: Boolean?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = underlined,
            undo = undoable,
            getFormatTable = CellFormatTable::textUnderlinedTable,
            updateCellFormatTable = CellFormatTable::setTextUnderlinedTable
        )
    }

    override fun setUnderlinedOnSelectedCells(underlined: Boolean?, undoable: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = underlined,
            undo = undoable,
            getFormatTable = CellFormatTable::textUnderlinedTable,
            updateCellFormatTable = CellFormatTable::setTextUnderlinedTable
        )
    }

    override fun setCellTextCrossed(cellId: CellId, crossed: Boolean?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = crossed,
            undo = undoable,
            getFormatTable = CellFormatTable::textCrossedTable,
            updateCellFormatTable = CellFormatTable::setTextCrossedTable
        )
    }

    override fun setCrossedOnSelectedCell(cellId: CellId, crossed: Boolean?, undoable: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = crossed,
            undo = undoable,
            getFormatTable = CellFormatTable::textCrossedTable,
            updateCellFormatTable = CellFormatTable::setTextCrossedTable
        )
    }


    override fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = alignment,
            undo = undoable,
            getFormatTable = CellFormatTable::textHorizontalAlignmentTable,
            updateCellFormatTable = CellFormatTable::setTextHorizontalAlignmentTable
        )
    }

    override fun setHorizontalAlignmentOnSelectedCells(
        cellId: CellId,
        alignment: TextHorizontalAlignment?,
        undoable: Boolean
    ) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            undo = undoable,
            getFormatTable = CellFormatTable::textHorizontalAlignmentTable,
            updateCellFormatTable = CellFormatTable::setTextHorizontalAlignmentTable
        )
    }

    override fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?, undoable: Boolean) {
        updateFormatOnOneCell(
            cellId = cellId,
            formatValue = alignment,
            undo = undoable,
            getFormatTable = CellFormatTable::textVerticalAlignmentTable,
            updateCellFormatTable = CellFormatTable::setTextVerticalAlignmentTable
        )
    }

    override fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?, undoable: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            undo = undoable,
            getFormatTable = CellFormatTable::textVerticalAlignmentTable,
            updateCellFormatTable = CellFormatTable::setTextVerticalAlignmentTable
        )
    }
}
