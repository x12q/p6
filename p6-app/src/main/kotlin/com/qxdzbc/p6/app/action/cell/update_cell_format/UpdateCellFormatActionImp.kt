package com.qxdzbc.p6.app.action.cell.update_cell_format

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.CellId
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.format2.CellFormatTable2
import com.qxdzbc.p6.ui.format2.FormatTable
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(scope = P6AnvilScope::class)
class UpdateCellFormatActionImp @Inject constructor(
//    private val cellFormatTableMs: Ms<CellFormatFlyweightTable>,
    private val stateContainerSt: St<@JvmSuppressWildcards StateContainer>,
) : UpdateCellFormatAction {

    private val sc by stateContainerSt
//    private val cTable by cellFormatTableMs

    private fun <T> updateFormat(
        cellId: CellId,
        formatValue: T?,
        getFormatTable: (CellFormatTable2) -> FormatTable<T>,
        updateCellFormatTable: (FormatTable<T>, CellFormatTable2) -> CellFormatTable2
    ) {
        sc.getCellFormatTable2Ms(cellId)?.also { fmtMs ->
            val newTable = getFormatTable(fmtMs.value)
                .removeValue(cellId.address)
                .let {
                    if (formatValue != null) {
                        it.addValue(cellId.address, formatValue)
                    } else {
                        it
                    }
                }
            fmtMs.value = updateCellFormatTable(newTable, fmtMs.value)
        }
    }

    fun <T> updateFormatOfSelectedCells(
        formatValue: T?,
        getFormatTable: (CellFormatTable2) -> FormatTable<T>,
        updateCellFormatTable: (FormatTable<T>, CellFormatTable2) -> CellFormatTable2
    ) {
        sc.getActiveCursorState()?.also { csst ->
            sc.getCellFormatTable2Ms(csst)?.also { fmtMs ->
                fmtMs.value = updateCellFormatTableWithNewFormatValueOnSelectedCells(
                    formatValue, csst,fmtMs.value,
                    getFormatTable,updateCellFormatTable
                )
            }
        }
    }

    fun <T> updateCellFormatTableWithNewFormatValueOnSelectedCells(
        formatValue: T?,
        cursorState: CursorState,
        currentFormatTable: CellFormatTable2,
        getFormatTable: (CellFormatTable2) -> FormatTable<T>,
        updateCellFormatTable: (FormatTable<T>, CellFormatTable2) -> CellFormatTable2
    ): CellFormatTable2 {
        val csst = cursorState
        val newTable = getFormatTable(currentFormatTable)
            .removeValueFromMultiRanges(csst.allRanges)
            .removeValueFromMultiCells(csst.allFragCells)
            .let {
                if (formatValue != null) {
                    it.addValueForMultiRanges(csst.allRanges, formatValue)
                        .addValueForMultiCells(csst.allFragCells, formatValue)
                } else {
                    it
                }
            }
        return updateCellFormatTable(newTable, currentFormatTable)
    }


    override fun setCellBackgroundColor(cellId: CellId, color: Color?) {
        updateFormat(
            cellId = cellId,
            formatValue = color,
            getFormatTable = { c -> c.cellBackgroundColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setCellBackgroundColorTable(newTable)
            }
        )
    }

    override fun setBackgroundColorOnSelectedCells(color: Color?) {
        updateFormatOfSelectedCells(
            formatValue = color,
            getFormatTable = { it.cellBackgroundColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setCellBackgroundColorTable(newTable)
            }
        )
    }

    override fun setCellTextSize(cellId: CellId, textSize: Float?) {
        updateFormat(
            cellId = cellId,
            formatValue = textSize,
            getFormatTable = { c -> c.textSizeTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextSizeTable(newTable)
            }
        )
    }

    override fun setSelectedCellsTextSize(textSize: Float?) {
        updateFormatOfSelectedCells(
            formatValue = textSize,
            getFormatTable = { it.textSizeTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextSizeTable(newTable)
            }
        )
    }

    override fun setCellTextColor(cellId: CellId, color: Color?) {
        updateFormat(
            cellId = cellId,
            formatValue = color,
            getFormatTable = { c -> c.textColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextColorTable(newTable)
            }
        )
    }

    override fun setSelectedCellsTextColor(color: Color?) {
        updateFormatOfSelectedCells(
            formatValue = color,
            getFormatTable = { it.textColorTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextColorTable(newTable)
            }
        )
    }

    override fun setCellTextUnderlined(cellId: CellId, underlined: Boolean?) {
        updateFormat(
            cellId = cellId,
            formatValue = underlined,
            getFormatTable = { c -> c.textUnderlinedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextUnderlinedTable(newTable)
            }
        )
    }

    override fun setUnderlinedOnSelectedCells(underlined: Boolean?) {
        updateFormatOfSelectedCells(
            formatValue = underlined,
            getFormatTable = { it.textUnderlinedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextUnderlinedTable(newTable)
            }
        )
    }

    override fun setCellTextCrossed(cellId: CellId, crossed: Boolean?) {
        updateFormat(
            cellId = cellId,
            formatValue = crossed,
            getFormatTable = { c -> c.textCrossedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextCrossedTable(newTable)
            }
        )
    }

    override fun setCrossedOnSelectedCell(cellId: CellId, crossed: Boolean?) {
        updateFormatOfSelectedCells(
            formatValue = crossed,
            getFormatTable = { it.textCrossedTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextCrossedTable(newTable)
            }
        )
    }


    override fun setCellHorizontalAlignment(cellId: CellId, alignment: TextHorizontalAlignment?) {
        updateFormat(
            cellId = cellId,
            formatValue = alignment,
            getFormatTable = { c -> c.textHorizontalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextHorizontalAlignmentTable(newTable)
            }
        )
    }

    override fun setHorizontalAlignmentOnSelectedCells(cellId: CellId, alignment: TextHorizontalAlignment?) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            getFormatTable = { it.textHorizontalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextHorizontalAlignmentTable(newTable)
            }
        )
    }

    override fun setCellVerticalAlignment(cellId: CellId, alignment: TextVerticalAlignment?) {
        updateFormat(
            cellId = cellId,
            formatValue = alignment,
            getFormatTable = { c -> c.textVerticalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextVerticalAlignmentTable(newTable)
            }
        )
    }

    override fun setVerticalAlignmentOnSelectedCells(cellId: CellId, alignment: TextVerticalAlignment?) {
        updateFormatOfSelectedCells(
            formatValue = alignment,
            getFormatTable = { it.textVerticalAlignmentTable },
            updateCellFormatTable = { newTable, fmt ->
                fmt.setTextVerticalAlignmentTable(newTable)
            }
        )
    }
}
