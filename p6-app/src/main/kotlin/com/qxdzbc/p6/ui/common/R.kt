package com.qxdzbc.p6.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.app.build.DebugFunctions.debug
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraintImp
import java.awt.Cursor
object R {
    const val defaultPythonConfigFile = "config/p6PythonDefaultConfig.json"

    object worksheetValue {
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

    object shape {
        val stdBoxShape = RoundedCornerShape(3.dp)
        val textFieldShape = stdBoxShape
        val buttonShape = stdBoxShape
    }

    object border {
        object mod {
            val black = Modifier.border(1.dp, Color.Black)
            val red = Modifier.border(1.dp, Color.Red)
            @Composable
            fun textFieldBorderMod()=Modifier.border(1.dp,color= MaterialTheme.colors.onPrimary, shape = R.shape.textFieldShape)
        }
    }

    object size {
        object mod {
            @Deprecated("use Modifier.fillMaxSize() instead")
            val fillMaxSize = Modifier.fillMaxHeight().fillMaxWidth()
            val zeroSize = Modifier.size(0.dp, 0.dp)
        }

        object value {
            val defaultColumnWidth = 90
            val defaultRowHeight = 26
            val rowRulerWidth = 50
            val minTabWidth = 30
            val minTabWidth2 = 90
            val maxTabWidth = 150

            val minWorkbookTabWidth = 30
            val maxWorkbookTabWidth = 150
            val tabHeight = 30
            val defaultCellSize = DpSize(defaultColumnWidth.dp, defaultRowHeight.dp)
            val defaultResizeCursorThickness = 1
            val defaultResizeCursorThumbThickness = defaultResizeCursorThickness * 5
            val resizerThickness = defaultResizeCursorThumbThickness * 2
        }
    }

    object padding {
        object value {
            val betweenButton = 15.dp
        }
    }

    object composite {
        object mod {
            val stdBar = Modifier.height(30.dp).fillMaxWidth()
        }
    }

    object text {
        object mod {
            val smallBoxPadding = Modifier.padding(start = 10.dp, end = 10.dp)
        }

        const val plus = "+"
        const val renameSheetDialogTitle = "Rename sheet"
    }

    object color {
        val textSelectionColor = Color(0xFF4286F4)
        val dBlack get() = Color.Black.debug()
        val dRed get() = Color.Red.debug()
        val dGreen get() = Color.Green.debug()
        val dMagenta get() = Color.Magenta.debug()
        val textSelectionColors = TextSelectionColors(
            handleColor = color.textSelectionColor,
            backgroundColor = color.textSelectionColor.copy(alpha = 0.4f)
        )
        @Composable
        fun radioButtonColor() : RadioButtonColors =RadioButtonDefaults.colors(
            selectedColor = MaterialTheme.colors.onPrimary,
            unselectedColor = MaterialTheme.colors.onPrimary,
            disabledColor = MaterialTheme.colors.onPrimary,
        )
    }

    object mouse {
        val downResize = PointerIcon(Cursor(Cursor.S_RESIZE_CURSOR))
        val leftResize = PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR))
        val rightResize = PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))
        val upResize = PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))
    }
}
