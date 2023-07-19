package com.qxdzbc.p6.ui.common.common_objects

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Icons used in the app. This contains UI that cannot be stored as files.
 */
object P6Icons {
    private var _formatColorText: ImageVector? = null
    val FormatColorText: ImageVector
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
