package com.qxdzbc.p6.ui.document.cell.state

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.document.cell.state.format.*
import com.qxdzbc.p6.ui.document.cell.state.format.cell.CellFormat

/**
 * It does not hold data object (DCell), but only appearance format data
 */
interface CellState {
    val address:CellAddress
    val cell: Cell?
    val cellMs:Ms<Cell>?
    fun setCellMs(cellMs:Ms<Cell>):CellState
    fun removeDataCell():CellState

    val textFormatMs:Ms<TextFormat>
    var textFormat:TextFormat

    val fontStyle:FontStyle
    fun setFontStyle(style:FontStyle):CellState

    val textColor:Color
    fun setTextColor(hexColor:ULong):CellState
    fun setTextColor(color:Color):CellState

    val alignment:Alignment
    fun setVerticalAlignment(alignment:TextVerticalAlignment):CellState
    fun setHorizontalAlignment(alignment:TextHorizontalAlignment):CellState

    val isTextCrossed:Boolean
    fun setTextCrossed(i:Boolean):CellState

    val isTextUnderlined:Boolean
    fun setTextUnderlined(i:Boolean):CellState

    val fontWeight:FontWeight
    fun setFontWeight(i:FontWeight):CellState

    val textStyle:TextStyle

    val cellFormatMs:Ms<CellFormat>
    var cellFormat:CellFormat

    val backgroundColor:Color
    fun setBackgroundColor(hexColor:ULong):CellState
    fun setBackgroundColor(color:Color):CellState
}