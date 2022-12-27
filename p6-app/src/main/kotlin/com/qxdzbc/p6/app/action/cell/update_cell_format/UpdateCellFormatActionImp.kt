package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
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


    private fun <T> updateFormat(
        cellId: CellId,
        formatValue: T?,
        undo: Boolean,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (FormatTable<T>, CellFormatTable) -> CellFormatTable
    ) {
        sc.getCellFormatTable2Ms(cellId)?.also { fmtMs ->
            fun runSetFormat(fv: T?) {
                val newTable = getFormatTable(fmtMs.value)
                    .removeValue(cellId.address)
                    .let {
                        if (fv != null) {
                            it.addValue(cellId.address, fv)
                        } else {
                            it
                        }
                    }
                fmtMs.value = updateCellFormatTable(newTable, fmtMs.value)
            }
            if (undo) {
                sc.getWbState(cellId.wbKeySt)?.also { wbState ->
                    val oldFormatValue = getFormatTable(fmtMs.value).getFirstValue(cellId.address)
                    wbState.commandStackMs.value = wbState.commandStack.add(
                        Commands.makeCommand(
                            run = { runSetFormat(formatValue) },
                            undo = { runSetFormat(oldFormatValue) }
                        )
                    )
                }
            }
            runSetFormat(formatValue)
        }
    }

    fun <T> updateFormatOfSelectedCells(
        formatValue: T?,
        undo: Boolean,
        getFormatTable: (CellFormatTable) -> FormatTable<T>,
        updateCellFormatTable: (FormatTable<T>, CellFormatTable) -> CellFormatTable
    ) {
        sc.getActiveCursorState()?.also { csst ->
            sc.getCellFormatTable2Ms(csst)?.also { fmtMs ->
                if (undo) {
                    val oldFormatConfig1 = getFormatTable(fmtMs.value).getMultiValueFromRangesIncludeNullFormat(csst.allRanges)
                    val oldFormatConfig2 = getFormatTable(fmtMs.value).getMultiValueFromCellsIncludeNullFormat(csst.allFragCells)
                    sc.getWbState(csst.wbKeySt)?.also {
                        it.commandStackMs.value = it.commandStack.add(Commands.makeCommand(
                            run = {
                                fmtMs.value = updateCellFormatTableWithNewFormatValueOnSelectedCells(
                                    formatValue = formatValue,
                                    ranges = csst.allRanges,
                                    cells = csst.allFragCells,
                                    currentFormatTable = fmtMs.value,
                                    getFormatTable = getFormatTable,
                                    updateCellFormatTable = updateCellFormatTable
                                )
                            },
                            undo = {
                                val newCellFormatTable= (oldFormatConfig1 + oldFormatConfig2).fold(fmtMs.value) { acc, (rangeSet, t) ->
                                    updateCellFormatTableWithNewFormatValueOnSelectedCells(
                                        formatValue = t,
                                        ranges = rangeSet.ranges,
                                        cells = emptyList(),
                                        currentFormatTable = acc,
                                        getFormatTable = getFormatTable,
                                        updateCellFormatTable = updateCellFormatTable
                                    )
                                }
                                fmtMs.value = newCellFormatTable
                            }
                        ))
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
        updateCellFormatTable: (FormatTable<T>, CellFormatTable) -> CellFormatTable
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
        return updateCellFormatTable(newTable, currentFormatTable)
    }


    override fun setCellBackgroundColor(cellId: CellId, color: Color?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            formatValue = color,
            undo = undo,
            getFormatTable = { c -> c.cellBackgroundColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setCellBackgroundColorTable(newTable)
            }
        )
    }

    override fun setBackgroundColorOnSelectedCells(color: Color?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = color,
            undo = undo,
            getFormatTable = { it.cellBackgroundColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setCellBackgroundColorTable(newTable)
            }
        )
    }

    override fun setCellTextSize(cellId: CellId, textSize: Float?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            undo = undo,
            formatValue = textSize,
            getFormatTable = { c -> c.textSizeTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextSizeTable(newTable)
            }
        )
    }

    override fun setSelectedCellsTextSize(textSize: Float?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = textSize,
            undo = undo,
            getFormatTable = { it.textSizeTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextSizeTable(newTable)
            }
        )
    }

    override fun setCellTextColor(cellId: CellId, color: Color?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            undo = undo,
            formatValue = color,
            getFormatTable = { c -> c.textColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextColorTable(newTable)
            }
        )
    }

    override fun setTextColorOnSelectedCells(color: Color?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = color,
            undo = undo,
            getFormatTable = { it.textColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextColorTable(newTable)
            }
        )
    }

    override fun setCellTextUnderlined(cellId: CellId, underlined: Boolean?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            formatValue = underlined,
            undo = undo,
            getFormatTable = { c -> c.textUnderlinedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextUnderlinedTable(newTable)
            }
        )
    }

    override fun setUnderlinedOnSelectedCells(underlined: Boolean?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = underlined,
            undo = undo, getFormatTable = { it.textUnderlinedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextUnderlinedTable(newTable)
            }
        )
    }

    override fun setCellTextCrossed(cellId: CellId, crossed: Boolean?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            formatValue = crossed,
            undo = undo,
            getFormatTable = { c -> c.textCrossedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextCrossedTable(newTable)
            }
        )
    }

    override fun setCrossedOnSelectedCell(cellId: CellId, crossed: Boolean?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = crossed,
            undo = undo, getFormatTable = { it.textCrossedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextCrossedTable(newTable)
            }
        )
    }


    override fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            formatValue = alignment,
            undo = undo,
            getFormatTable = { c -> c.textHorizontalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextHorizontalAlignmentTable(newTable)
            }
        )
    }

    override fun setHorizontalAlignmentOnSelectedCells(
        cellId: CellId,
        alignment: TextHorizontalAlignment?,
        undo: Boolean
    ) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            undo = undo, getFormatTable = { it.textHorizontalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextHorizontalAlignmentTable(newTable)
            }
        )
    }

    override fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?, undo: Boolean) {
        updateFormat(
            cellId = cellId,
            formatValue = alignment,
            undo = undo,
            getFormatTable = { c -> c.textVerticalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextVerticalAlignmentTable(newTable)
            }
        )
    }

    override fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?, undo: Boolean) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            undo = undo, getFormatTable = { it.textVerticalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextVerticalAlignmentTable(newTable)
            }
        )
    }
}
