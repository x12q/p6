package com.emeraldblast.p6.ui.document.worksheet.action

import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.ui.document.worksheet.ruler.actions.RulerAction

interface WorksheetActionTable{
    val worksheetAction: WorksheetAction
    val cursorAction: CursorAction
    val cellEditorAction: CellEditorAction
    val rowRulerAction: RulerAction
    val colRulerAction: RulerAction
}
