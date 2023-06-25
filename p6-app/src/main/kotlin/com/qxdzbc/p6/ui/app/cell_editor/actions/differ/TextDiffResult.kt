package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import androidx.compose.ui.text.TextRange

/**
 * Currently not used anywhere
 */
sealed class TextDiffResult{
    data class Addition(val addition:TextAndRange):TextDiffResult()
    data class Deletion(val deleteRange:TextRange):TextDiffResult()
    data class TextSelectionWasChanged(val oldSelection:TextRange,val newSelection:TextRange):TextDiffResult()
    object NoChange:TextDiffResult()
}
