package com.emeraldblast.p6.app.action.worksheet.release_focus

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.document.workbook.WorkbookKey

/**
 * Restore focus state of a worksheet to default state, including:
 * - cell editor is not opened, not focused
 * - cursor is focused
 */
interface RestoreWindowFocusState {
    fun restoreCellEditorAndCursorState():Rse<Unit>
    fun restoreAllWsFocusIfAllow():Rse<Unit>
    fun setFocusConsideringRangeSelectorAllWindow():Rse<Unit>
    fun setFocusStateConsideringRangeSelector(wbKey: WorkbookKey): Rse<Unit>
}
