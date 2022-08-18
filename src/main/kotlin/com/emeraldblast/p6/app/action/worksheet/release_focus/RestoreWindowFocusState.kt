package com.emeraldblast.p6.app.action.worksheet.release_focus

import com.emeraldblast.p6.app.common.utils.Rse
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

/**
 * Restore focus state of a worksheet to default state, including:
 * - cell editor is not opened, not focused
 * - cursor is focused
 */
interface RestoreWindowFocusState {
    fun restoreCellEditorAndCursorState(): Rse<Unit>
    fun restoreAllWsFocusIfAllow(): Rse<Unit>

    /**
     * if range selector is allowed, focus on editor, otherwise focus on cursor, free focus on editor
     */
    fun setFocusConsideringRangeSelectorAllWindow(): Rse<Unit>

    /**
     * if range selector is allowed, focus on editor, otherwise focus on cursor, free focus on editor
     */
    fun setFocusStateConsideringRangeSelector(wbKey: WorkbookKey): Rse<Unit>
}
