package com.qxdzbc.p6.ui.worksheet.action

import com.qxdzbc.p6.composite_actions.worksheet.WorksheetAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.action.ThumbAction
import com.qxdzbc.p6.ui.worksheet.ruler.actions.RulerAction

interface WorksheetActionTable{
    val worksheetAction: WorksheetAction
    val cursorAction: CursorAction
    val cellEditorAction: CellEditorAction
    val rowRulerAction: RulerAction
    val colRulerAction: RulerAction
    val thumbAction: ThumbAction
}
