package com.qxdzbc.p6.app.action.cell_editor.run_formula

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell_editor.close_cell_editor.CloseCellEditorAction
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.common.utils.TextUtils
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class RunFormulaOrSaveValueToCellActionImp @Inject constructor(
    private val cellLiteralParser: CellLiteralParser,
    private val updateCellAction: UpdateCellAction,
    private val stateContMs: Ms<StateContainer>,
    @PartialTreeExtractor
    val treeExtractor: TreeExtractor,
    val closeCellEditorAction: CloseCellEditorAction,
)  : RunFormulaOrSaveValueToCellAction {

    private val stateCont by stateContMs
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs


    override fun runFormulaOrSaveValueToCell() {
        val wsStateMs: Ms<WorksheetState>? = editorState.targetCursorId?.let { stateCont.getWsStateMs(it) }
        val ws = wsStateMs?.value?.worksheet
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        val editTarget = editorState.targetCell
        if (ws != null && wbKey != null && wsName != null && editTarget != null) {
            // x: execute the formula in the editor
            val cell = ws.getCell(editTarget)
            val editorText = editorState.rangeSelectorTextField?.text ?: editorState.currentText

            val reverseRequest = if (cell?.fullFormulaFromExUnit != null) {
                CellUpdateRequestDM(
                    cellId = CellIdDM(
                        wbKey = wbKey,
                        wsName = wsName,
                        address = editTarget,
                    ),
                    cellContent = CellContentDM.fromFormula(cell.fullFormulaFromExUnit)
                )
            } else {
                CellUpdateRequestDM(
                    cellId = CellIdDM(
                        wbKey = wbKey,
                        wsName = wsName,
                        address = editTarget,
                    ),
                    cellContent = CellContentDM.fromAny(cell?.currentValue)
                )
            }
            var value: String? = null
            var formula: String? = null
            if (TextUtils.isFormula(editorText)) {
                formula = editorText
            } else {
                value = editorText
            }
            val request = CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = wbKey,
                    wsName = wsName,
                    address = editTarget,
                ),
                cellContent = CellContentDM(
                    cellValue = CellValue.fromAny(cellLiteralParser.parse(value)),
                    formula = formula,
                    originalText = formula ?: value,
                )
            )

            val command = Commands.makeCommand(
                run = { updateCellAction.updateCellDM(request) },
                undo = { updateCellAction.updateCellDM(reverseRequest) }
            )
            stateCont.getUndoStackMs(ws)?.also { cMs->
                cMs.value = cMs.value.add(command)
            }
            command.run()
            closeCellEditorAction.closeEditor()
        }
    }
}
