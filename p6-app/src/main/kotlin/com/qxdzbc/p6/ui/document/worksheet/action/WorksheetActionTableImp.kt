package com.qxdzbc.p6.ui.document.worksheet.action

import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.document.worksheet.ruler.actions.RulerAction
import javax.inject.Inject

class WorksheetActionTableImp @Inject constructor(
    override val cursorAction: CursorAction,
    override val cellEditorAction: CellEditorAction,
    private val rulerAction: RulerAction,
    override val worksheetAction: WorksheetAction,
) : WorksheetActionTable {
    override val rowRulerAction: RulerAction = rulerAction
    override val colRulerAction: RulerAction = rulerAction

}
