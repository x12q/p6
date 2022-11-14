package com.qxdzbc.p6.app.action.worksheet.release_focus

import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

/**
 * Restore focus state of a worksheet to default state, including:
 * - cell editor is not opened, not focused
 * - cursor is focused
 */
interface RestoreWindowFocusState {
    fun restoreAllWindowFocusState(): Rse<Unit>
    fun restoreAllWsFocusIfRangeSelectorIsNotActive(): Rse<Unit>

    /**
     * if range selector is allowed, focus on editor, otherwise focus on cursor, free focus on editor
     */
    fun setFocusConsideringRangeSelectorAllWindow(): Rse<Unit>

    /**
     * if range selector is allowed, focus on editor, otherwise focus on cursor, free focus on editor
     */
    fun setFocusStateConsideringRangeSelector(wbKey: WorkbookKey): Rse<Unit>
}
