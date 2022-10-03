package com.qxdzbc.p6.ui.document.worksheet.action

import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction

interface WorksheetActionTable{
    val worksheetAction: WorksheetAction
    val cursorAction: CursorAction
    val cellEditorAction: CellEditorAction
    val rowRulerAction: RulerAction
    val colRulerAction: RulerAction
}
