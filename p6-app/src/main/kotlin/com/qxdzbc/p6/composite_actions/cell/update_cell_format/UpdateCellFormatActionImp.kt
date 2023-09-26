package com.qxdzbc.p6.composite_actions.cell.update_cell_format

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.composite_actions.cell.cell_update.CommonReactionWhenAppStatesChanged
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWs
import com.qxdzbc.p6.composite_actions.common_data_structure.WbWsSt
import com.qxdzbc.p6.command.BaseCommand
import com.qxdzbc.p6.command.Command
import com.qxdzbc.p6.document_data_layer.cell.CellId
import com.qxdzbc.p6.document_data_layer.cell.address.CellAddress
import com.qxdzbc.p6.document_data_layer.range.address.RangeAddress
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.format.CellFormatTable
import com.qxdzbc.p6.ui.format.FormatConfig
import com.qxdzbc.p6.ui.format.FormatTable
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(scope = P6AnvilScope::class)
class UpdateCellFormatActionImp @Inject constructor(
    private val stateContainerSt:StateContainer,
    val commonReactionWhenAppStatesChanged: CommonReactionWhenAppStatesChanged,
) : UpdateCellFormatAction {

    private val sc  = stateContainerSt

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

    /**
     * create an undoable command to update format of the cell at [cellId]
     */
    fun <T> makeCommandForFormatOneCell(
        cellId: CellId,
        formatValue: T?,
        cellFormatTableMs: Ms<CellFormatTable>,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ): Command {

        val command = object: BaseCommand(){
            val _formatValue = formatValue
            val _oldFormatValue = getFormatTable(cellFormatTableMs.value).getFormatValue(cellId.address)
            val _cellFormatTableMs = cellFormatTableMs
            val _cellId = cellId
            val _getFormatTable = getFormatTable
            val _updateCellFormatTable = updateCellFormatTable
            override fun run() {
                runSetFormat(
                    formatValue = _formatValue,
                    cellId = _cellId,
                    fmtMs = _cellFormatTableMs,
                    getFormatTable = _getFormatTable,
                    updateCellFormatTable = _updateCellFormatTable
                )
            }

            override fun undo() {
                runSetFormat(
                    formatValue = _oldFormatValue,
                    cellId = _cellId,
                    fmtMs = _cellFormatTableMs,
                    getFormatTable = _getFormatTable,
                    updateCellFormatTable = _updateCellFormatTable
                )
            }

        }
        return command
    }

    /**
     * Update format value for one cell at [cellId]
     */
    fun <T> updateFormatOnOneCell(
        cellId: CellId,
        formatValue: T?,
        undo: Boolean,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (CellFormatTable, FormatTable<T>) -> CellFormatTable
    ) {
        sc.getCellFormatTableMs(cellId)?.also { fmtMs ->
            if (undo) {
                sc.getUndoStackMs(cellId)?.also {
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
            val _cursorRanges = cursorState.allRanges
            val _cursorCells = cursorState.allFragCells
            val _cellFormatTableMs = cellFormatTableMs
            val _oldFormatConfig1 =
                getFormatTable(_cellFormatTableMs.value).getConfigSetFromRanges(_cursorRanges)
            val _oldFormatConfig2 =
                getFormatTable(_cellFormatTableMs.value).getConfigSetFromCells(_cursorCells)

            override fun run() {
                _cellFormatTableMs.value = updateCellFormatTableWithNewFormatValueOnSelectedCells(
                    formatValue = _formatValue,
                    ranges = _cursorRanges,
                    cells = _cursorCells,
                    currentFormatTable = _cellFormatTableMs.value,
                    getFormatTable = getFormatTable,
                    updateCellFormatTable = updateCellFormatTable
                )
            }

            override fun undo() {
                val newCellFormatTable =
                    (_oldFormatConfig1.all + _oldFormatConfig2.all).fold(cellFormatTableMs.value) { acc, (rangeSet, t) ->
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
                    sc.getUndoStackMs(csst)?.also {
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

    /**
     * update cell format table with new format value on selected cells.
     * @return a new cell format table by update format value of cells in [ranges] and [cells] with [formatValue]
     */
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

    /**
     * create an undoable Command for applying a format config to the cell format table at [wbWsSt]
     */
    fun makeCommandToApplyFormatConfig(wbWsSt: WbWsSt, config: FormatConfig): Command {
        val rt = object : BaseCommand() {
            val _wbWsSt = wbWsSt
            val newConfig = config
            val oldConfig = sc.getCellFormatTable(wbWsSt)
                ?.getFormatConfigForConfig_Respectively(config)
            val cellFormatTableMs get() = sc.getCellFormatTableMs(_wbWsSt)
            override fun run() {
                // apply new config
                cellFormatTableMs?.also {
                    it.value = it.value.applyConfig(newConfig)
                }
            }

            override fun undo() {
                cellFormatTableMs?.also {
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
            sc.getUndoStackMs(wbWsSt)?.also {
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

    fun makeClearFormatCommandUsingFormatConfig_Flat(wbWsSt: WbWsSt, config: FormatConfig): Command {
        val command = object : BaseCommand() {
            val _wbwsSt = wbWsSt
            val _oldConfig = sc.getCellFormatTable(_wbwsSt)?.getFormatConfigForConfig_Flat(config)
            val _config = config
            override fun run() {
                sc.getCellFormatTableMs(_wbwsSt)?.also {
                    it.value = it.value.removeFormatByConfig_Flat(_config)
                }
            }

            override fun undo() {
                _oldConfig?.also {
                    sc.getCellFormatTableMs(_wbwsSt)?.also {
                        it.value = it.value.applyConfig(_oldConfig)
                    }
                }
            }
        }
        return command
    }

    override fun clearFormat_Flat(wbWsSt: WbWsSt, config: FormatConfig, undoable: Boolean) {
        if (undoable) {
            sc.getUndoStackMs(wbWsSt)?.also {
                it.value = it.value.add(makeClearFormatCommandUsingFormatConfig_Flat(wbWsSt, config))
            }
        }
        sc.getCellFormatTableMs(wbWsSt)?.also {
            it.value = it.value.removeFormatByConfig_Flat(config)
        }
    }

    override fun clearFormat_Flat(wbWs: WbWs, config: FormatConfig, undoable: Boolean) {
        sc.getWbWsSt(wbWs)?.also {
            clearFormat_Flat(it,config,undoable)
        }
    }

    fun makeClearFormatCommandUsingFormatConfig_Respective(wbWsSt: WbWsSt, config: FormatConfig): Command {
        val command = object : BaseCommand() {
            val _wbWsSt=wbWsSt
            // x: it is essential to get a flat config here to ensure that format of all categories are collected for all ranges.
            val _oldConfig = sc.getCellFormatTable(_wbWsSt)?.getFormatConfigForConfig_Flat(config)
            val _config = config
            override fun run() {
                /*
                clear format at the target config
                 */
                sc.getCellFormatTableMs(_wbWsSt)?.also {
                    it.value = it.value.removeFormatByConfig_Respectively(_config)
                }
            }

            override fun undo() {
                /*
                restore the old config
                 */
                _oldConfig?.also {
                    sc.getCellFormatTableMs(_wbWsSt)?.also {
                        it.value = it.value.applyConfig(_oldConfig)
                    }
                }
            }
        }
        return command
    }

    override fun clearFormat_Respective(wbWsSt: WbWsSt, config: FormatConfig, undoable: Boolean) {
        if (undoable) {
            sc.getUndoStackMs(wbWsSt)?.also {
                it.value = it.value.add(makeClearFormatCommandUsingFormatConfig_Respective(wbWsSt, config))
            }
        }
        sc.getCellFormatTableMs(wbWsSt)?.also {
            it.value = it.value.removeFormatByConfig_Respectively(config)
        }
    }

    override fun clearFormat_Respective(wbWs: WbWs, config: FormatConfig, undoable: Boolean) {
        sc.getWbWsSt(wbWs)?.also {
            clearFormat_Respective(it,config,undoable)
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
