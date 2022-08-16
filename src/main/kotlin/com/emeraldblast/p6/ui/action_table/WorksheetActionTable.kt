package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.ui.document.worksheet.ruler.actions.RulerAction

interface WorksheetActionTable{
    fun getWorksheetAction(): WorksheetAction
    fun getCursorAction(): CursorAction
    fun getCellEditorAction(): CellEditorAction
    fun getRowRulerAction(): RulerAction
    fun getColRulerAction(): RulerAction
}
