package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

/**
 * Currently not used anywhere
 */
data class TextDiffResult(
    val type: TextDiffType,
    val addition: TextAndRange?,
    val replacement: TextAndRange?,
)
