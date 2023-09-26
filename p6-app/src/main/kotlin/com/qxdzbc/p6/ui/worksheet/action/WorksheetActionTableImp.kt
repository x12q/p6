package com.qxdzbc.p6.ui.worksheet.action

import com.qxdzbc.p6.composite_actions.worksheet.WorksheetAction
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import com.qxdzbc.p6.ui.worksheet.cursor.thumb.action.ThumbAction
import com.qxdzbc.p6.ui.worksheet.ruler.actions.RulerAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class WorksheetActionTableImp @Inject constructor(
    override val cursorAction: CursorAction,
    override val cellEditorAction: CellEditorAction,
    private val rulerAction: RulerAction,
    override val worksheetAction: WorksheetAction,
    override val thumbAction: ThumbAction,
    ) : WorksheetActionTable {
    override val rowRulerAction: RulerAction = rulerAction
    override val colRulerAction: RulerAction = rulerAction

}
