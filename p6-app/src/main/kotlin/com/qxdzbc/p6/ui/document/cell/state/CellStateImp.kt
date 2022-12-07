package com.qxdzbc.p6.ui.document.cell.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextFormat
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextHorizontalAlignment
import com.qxdzbc.p6.ui.document.cell.state.format.text.TextVerticalAlignment
import com.qxdzbc.p6.ui.format.cell_format.TextFormat3

data class CellStateImp(
    override val address: CellAddress,
    override val cellMs: Ms<Cell>?,
    override val textFormatMs: Ms<TextFormat> = ms(TextFormat.default),
    override val cellFormatMs: Ms<CellFormat> = ms(CellFormat.default),
    override val textFormat3Ms: Ms<TextFormat3> = ms(TextFormat3.defaultCellFormat),
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

    override var textFormat: TextFormat by textFormatMs

    override val textFormat3: TextFormat3
        get() = textFormat3Ms.value

    override fun setVerticalAlignment(alignment: TextVerticalAlignment): CellState {
        textFormat = textFormat.copy(verticalAlignment = alignment)
        return this
    }

    override fun setHorizontalAlignment(alignment: TextHorizontalAlignment): CellState {
        textFormat = textFormat.copy(horizontalAlignment = alignment)
        return this
    }

    override val alignment: Alignment
        get() = textFormat.alignment
    override val isTextCrossed: Boolean
        get() = textFormat.isCrossed

    override fun setTextCrossed(i: Boolean): CellState {
        textFormat = textFormat.copy(isCrossed = i)
        return this
    }

    override val isTextUnderlined: Boolean
        get() = textFormat.isUnderlined

    override fun setTextUnderlined(i: Boolean): CellState {
        textFormat = textFormat.copy(isUnderlined = i)
        return this
    }

    override val fontWeight: FontWeight
        get() = textFormat.fontWeight

    override fun setFontWeight(i: FontWeight): CellState {
        textFormat = textFormat.copy(fontWeight = i)
        return this
    }

    override val textStyle: TextStyle
        get() = textFormat3Ms.value.toTextStyle()

    override var cellFormat: CellFormat by cellFormatMs
    override val backgroundColor: Color
        get() = cellFormat.backgroundColor

    override fun setBackgroundColor(hexColor: ULong): CellState {
        return this.setBackgroundColor(Color(hexColor))
    }

    override fun setBackgroundColor(color: Color): CellState {
        cellFormat = cellFormat.copy(backgroundColor = color)
        return this
    }
}
