package com.emeraldblast.p6.ui.action_table

import com.emeraldblast.p6.app.action.worksheet.WorksheetAction
import com.emeraldblast.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.emeraldblast.p6.ui.app.cell_editor.in_cell.actions.CellEditorAction
import com.emeraldblast.p6.ui.document.worksheet.ruler.actions.RulerAction
import javax.inject.Inject

class WorksheetActionTableImp @Inject constructor(
    private val wsAction: WorksheetAction,
    private val cursorAction: CursorAction,
    private val cellEditorAction: CellEditorAction,
    private val rulerAction: RulerAction,
) : WorksheetActionTable {


    override fun getWorksheetAction(): WorksheetAction {
        return wsAction
    }

    override fun getCursorAction(): CursorAction {
        return cursorAction
    }

    override fun getCellEditorAction(): CellEditorAction {
        return cellEditorAction
    }

    override fun getRowRulerAction(): RulerAction {
        return rulerAction
    }

    override fun getColRulerAction(): RulerAction {
        return rulerAction
    }
}
