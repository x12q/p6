package com.qxdzbc.p6.ui.document.cell.state

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment

data class CellStateImp(
    override val address: CellAddress,
    override val cellMs: Ms<Cell>?,
    override var textFormat: TextFormat? = null,
    override var cellFormat: CellFormat? = null,
) : CellState {
    init {
        if (cellMs != null) {
            if (address != cellMs.value.address) {
                throw IllegalArgumentException("CellState at ${address} cannot point to cell at ${cellMs.value.address}")
            }
        }
    }

    override val cell: Cell? get() = cellMs?.value
    override fun setCellMs(cellMs: Ms<Cell>): CellState {
        return this.copy(cellMs = cellMs)
    }

    override fun removeDataCell(): CellState {
        return this.copy(cellMs = null)
    }

    override fun setTextFormat(i: TextFormat?): CellStateImp {
        return this.copy(textFormat = i)
    }

    override fun setVerticalAlignment(alignment: TextVerticalAlignment): CellState {
        textFormat = textFormatOrCreateNew.setVerticalAlignment(i = alignment)
        return this
    }

    override fun setHorizontalAlignment(alignment: TextHorizontalAlignment): CellState {
        textFormat = textFormatOrCreateNew.setHorizontalAlignment(i = alignment)
        return this
    }

    override val alignment: Alignment?
        get() = textFormat?.alignment
    override val isTextCrossed: Boolean?
        get() = textFormat?.isCrossed

    override fun setTextCrossed(i: Boolean): CellState {
        textFormat = textFormatOrCreateNew.setCrossed(i = i)
        return this
    }

    override val isTextUnderlined: Boolean?
        get() = textFormat?.isUnderlined

    override fun setTextUnderlined(i: Boolean): CellState {
        textFormat = textFormatOrCreateNew.setUnderlined(i = i)
        return this
    }

    override val fontWeight: FontWeight?
        get() = textFormat?.fontWeight

    private inline val textFormatOrCreateNew: TextFormat
        get() {
            return textFormat ?: TextFormat.createDefaultTextFormat()
        }

    override fun setFontWeight(i: FontWeight): CellState {
        textFormat = textFormatOrCreateNew.setFontWeight(i = i)
        return this
    }

    override val textStyle: TextStyle
        get() = textFormat?.toTextStyle() ?: CellStates.defaultTextStyle

    override fun setCellFormat(i: CellFormat?): CellStateImp {
        return this.copy(cellFormat = i)
    }

    override val backgroundColor: Color?
        get() = cellFormat?.backgroundColor

    override fun setBackgroundColor(hexColor: ULong): CellState {
        return this.setBackgroundColor(Color(hexColor))
    }

    override fun setBackgroundColor(color: Color): CellState {
        cellFormat = cellFormat?.setBackgroundColor(color)
        return this
    }
}
