package com.qxdzbc.p6.ui.worksheet

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.worksheet.state.RangeConstraintImp

object WorksheetConstants {
    val defaultColumnWidth = 90.dp
    val defaultRowHeight = 26.dp
    val rowRulerWidth = 50.dp
    val defaultCellSize = DpSize(defaultColumnWidth, defaultRowHeight)
    val defaultResizeCursorThickness = 1.dp
    val defaultResizeCursorThumbThickness = defaultResizeCursorThickness * 5
    val resizerThickness = defaultResizeCursorThumbThickness * 2

    val smallColRange = 1..15
    val smallRowRange = smallColRange
    val defaultColRange = 1..1_100_000
    val defaultRowRange = defaultColRange
    val rowLimit = defaultRowRange.last
    val colLimit = defaultColRange.last
    val defaultVisibleColRange = 1..20//15
    val defaultVisibleRowRange = 1..20//18
    val defaultRangeConstraint = RangeConstraintImp(defaultColRange, defaultRowRange)
}