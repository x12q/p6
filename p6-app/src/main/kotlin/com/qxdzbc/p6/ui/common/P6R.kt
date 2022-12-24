package com.qxdzbc.p6.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.build.DebugFunctions.debug
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraintImp
import java.awt.Cursor
object P6R {
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
            val cursorBorder = Modifier.border(2.dp,color.cursorColor)
            val black = Modifier.border(1.dp, Color.Black)
            val red = Modifier.border(1.dp, Color.Red)
        }
    }

    object canvas{
        object stroke{
            val dashLine = Stroke(
                width = 2.0f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
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
        object mod{
            val stdTextFieldPadding = Modifier.padding(5.dp)
        }
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
        val cursorColor = Color.Blue
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
        object icon{
            val downResize = PointerIcon(Cursor(Cursor.S_RESIZE_CURSOR))
            val leftResize = PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR))
            val rightResize = PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))
            val upResize = PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR))
            val thumbMouseIcon = PointerIcon(Cursor(Cursor.CROSSHAIR_CURSOR))
        }
    }

    object icons{
        private var _formatColorText: ImageVector? = null
        public val FormatColorText: ImageVector
            get() {
                if (_formatColorText != null) {
                    return _formatColorText!!
                }
                _formatColorText = materialIcon(name = "Filled.FormatColorText") {
                    materialPath {
//                        moveTo(2.0f, 20.0f)
//                        horizontalLineToRelative(20.0f)
//                        verticalLineToRelative(4.0f)
//                        horizontalLineTo(2.0f)
//                        verticalLineTo(20.0f)
//                        close()
                        moveTo(5.49f, 17.0f)
                        horizontalLineToRelative(2.42f)
                        lineToRelative(1.27f, -3.58f)
                        horizontalLineToRelative(5.65f)
                        lineTo(16.09f, 17.0f)
                        horizontalLineToRelative(2.42f)
                        lineTo(13.25f, 3.0f)
                        horizontalLineToRelative(-2.5f)
                        lineTo(5.49f, 17.0f)
                        close()
                        moveTo(9.91f, 11.39f)
                        lineToRelative(2.03f, -5.79f)
                        horizontalLineToRelative(0.12f)
                        lineToRelative(2.03f, 5.79f)
                        horizontalLineTo(9.91f)
                        close()
                    }
                }
                return _formatColorText!!
            }
        private var _formatColorFill: ImageVector? = null
        public val FormatColorFill: ImageVector
            get() {
                if (_formatColorFill != null) {
                    return _formatColorFill!!
                }
                _formatColorFill = materialIcon(name = "Filled.FormatColorFill2") {
                    materialPath {
                        moveTo(16.56f, 8.94f)
                        lineTo(7.62f, 0.0f)
                        lineTo(6.21f, 1.41f)
                        lineToRelative(2.38f, 2.38f)
                        lineTo(3.44f, 8.94f)
                        curveToRelative(-0.59f, 0.59f, -0.59f, 1.54f, 0.0f, 2.12f)
                        lineToRelative(5.5f, 5.5f)
                        curveTo(9.23f, 16.85f, 9.62f, 17.0f, 10.0f, 17.0f)
                        reflectiveCurveToRelative(0.77f, -0.15f, 1.06f, -0.44f)
                        lineToRelative(5.5f, -5.5f)
                        curveTo(17.15f, 10.48f, 17.15f, 9.53f, 16.56f, 8.94f)
                        close()
                        moveTo(5.21f, 10.0f)
                        lineTo(10.0f, 5.21f)
                        lineTo(14.79f, 10.0f)
                        horizontalLineTo(5.21f)
                        close()
                        moveTo(19.0f, 11.5f)
                        curveToRelative(0.0f, 0.0f, -2.0f, 2.17f, -2.0f, 3.5f)
                        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                        reflectiveCurveToRelative(2.0f, -0.9f, 2.0f, -2.0f)
                        curveTo(21.0f, 13.67f, 19.0f, 11.5f, 19.0f, 11.5f)
                        close()
//                        moveTo(2.0f, 20.0f)
//                        horizontalLineToRelative(20.0f)
//                        verticalLineToRelative(4.0f)
//                        horizontalLineTo(2.0f)
//                        verticalLineTo(20.0f)
//                        close()
                    }
                }
                return _formatColorFill!!
            }
    }
}
