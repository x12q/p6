package com.qxdzbc.p6.app.action.cell_editor.run_formula

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell_editor.close_cell_editor.CloseCellEditorAction
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.common.utils.TextUtils
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.app.document.cell.address.CellAddress
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
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class RunFormulaOrSaveValueToCellActionImp @Inject constructor(
    private val cellLiteralParser: CellLiteralParser,
    private val updateCellAction: UpdateCellAction,
    private val stateCont:StateContainer,
    @PartialTreeExtractor
    val treeExtractor: TreeExtractor,
    val closeCellEditorAction: CloseCellEditorAction,
) : RunFormulaOrSaveValueToCellAction {

   
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs


    override fun runFormulaOrSaveValueToCell(undoable:Boolean) {
        val wsState: WorksheetState? = editorState.targetCursorId?.let { stateCont.getWsState(it) }
        val ws = wsState?.worksheet
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        val editTarget = editorState.targetCell
        if (ws != null && wbKey != null && wsName != null && editTarget != null) {
            // x: execute the formula in the editor
            val cell = ws.getCell(editTarget)
            val editorText = editorState.rangeSelectorTextField?.text ?: editorState.currentText

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
            if(undoable){
                val command = makeCommandToRunFormulaOrSaveValueToCell()
                command?.also {
                    stateCont.getUndoStackMs(ws)?.also { cMs ->
                        cMs.value = cMs.value.add(command)
                    }
                }
            }
            updateCellAction.updateCellDM(request)
            closeCellEditorAction.closeEditor()
        }
    }

    fun makeCommandToRunFormulaOrSaveValueToCell(): Command? {
        val wsState: WorksheetState? = editorState.targetCursorId?.let { stateCont.getWsState(it) }
        val ws = wsState?.worksheet
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        val editTarget = editorState.targetCell
        if (ws != null && wbKey != null && wsName != null && editTarget != null) {
            // x: execute the formula in the editor
            // x: execute the formula in the editor
            val cell = ws.getCell(editTarget)
            val editorText = editorState.rangeSelectorTextField?.text ?: editorState.currentText

            var value: String? = null
            var formula: String? = null
            if (TextUtils.isFormula(editorText)) {
                formula = editorText
            } else {
                value = editorText
            }
            val command = object : BaseCommand() {
                val _cell: Cell? = cell
                val _targetWbKeySt = editorState.targetCursorId?.wbKeySt
                val _targetWsNameSt = editorState.targetCursorId?.wsNameSt
                val _editTarget:CellAddress? = editorState.targetCell

                val _request:CellUpdateRequestDM? = run{
                    if(_targetWbKeySt != null && _targetWsNameSt!=null && this._editTarget !=null){
                        CellUpdateRequestDM(
                            cellId = CellIdDM(
                                wbKey = _targetWbKeySt.value,
                                wsName = _targetWsNameSt.value,
                                address = _editTarget,
                            ),
                            cellContent = CellContentDM(
                                cellValue = CellValue.fromAny(cellLiteralParser.parse(value)),
                                formula = formula,
                                originalText = formula ?: value,
                            )
                        )
                    }else{
                        null
                    }
                }

                val reverseRequest:CellUpdateRequestDM? = run{
                    if(_targetWbKeySt != null && _targetWsNameSt!=null && this._editTarget !=null){
                        if (_cell?.fullFormulaFromExUnit != null) {
                            CellUpdateRequestDM(
                                cellId = CellIdDM(
                                    wbKey = _targetWbKeySt.value,
                                    wsName = _targetWsNameSt.value,
                                    address = _editTarget,
                                ),
                                cellContent = CellContentDM.fromFormula(_cell.fullFormulaFromExUnit)
                            )
                        } else {
                            CellUpdateRequestDM(
                                cellId = CellIdDM(
                                    wbKey = _targetWbKeySt.value,
                                    wsName = _targetWsNameSt.value,
                                    address = _editTarget,
                                ),
                                cellContent = CellContentDM.fromAny(_cell?.currentValue)
                            )
                        }
                    }else{
                        null
                    }
                }
                override fun run() {
                    _request?.also {
                        updateCellAction.updateCellDM(it)
                    }
                }

                override fun undo() {
                    reverseRequest?.also {
                        updateCellAction.updateCellDM(it)
                    }
                }
            }
            return command
        }else{
            return null
        }
    }
}
