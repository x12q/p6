package com.qxdzbc.p6.ui.app.cell_editor.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.key_event.PKeyEvent
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayTextAction
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

class CellEditorActionImp @Inject constructor(
    private val cellLiteralParser: CellLiteralParser,
    private val updateCellAction: UpdateCellAction,
    private val cursorAction: CursorAction,
    private val makeDisplayText: MakeCellEditorDisplayTextAction,
    private val openCellEditor: OpenCellEditorAction,
    @StateContainerMs
    private val stateContMs: Ms<StateContainer>,
    private val textDiffer: TextDiffer,
    val cycleLockStateAct: CycleFormulaLockStateAction,
) : CellEditorAction,
    CycleFormulaLockStateAction by cycleLockStateAct,
    MakeCellEditorDisplayTextAction by makeDisplayText,
    OpenCellEditorAction by openCellEditor {

    private val stateCont by stateContMs
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs

    private fun isFormula(formula: String): Boolean {
        val script: String = formula.trim()
        val isFormula: Boolean = script.startsWith("=")
        return isFormula
    }

    override fun focus() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnEditor()
        }
    }

    override fun runFormulaOrSaveValueToCell() {
        val wsStateMs: Ms<WorksheetState>? = editorState.targetCursorId?.let { stateCont.getWsStateMs(it) }
        val ws = wsStateMs?.value?.worksheet
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        val editTarget = editorState.targetCell
        if (ws != null && wbKey != null && wsName != null && editTarget != null) {
            // x: execute the formula in the editor
            val cell = ws.getCell(editTarget)
            val codeText = editorState.rangeSelectorTextField?.text ?: editorState.currentText

            val reverseRequest = if (cell?.fullFormula != null) {
                CellUpdateRequestDM(
                    cellId = CellIdDM(
                        wbKey = wbKey,
                        wsName = wsName,
                        address = editTarget,
                    ),
                    cellContent = CellContentDM.fromFormula(cell.fullFormula)
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
            if (isFormula(codeText)) {
                formula = codeText
            } else {
                value = codeText
            }
            val request = CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = wbKey,
                    wsName = wsName,
                    address = editTarget,
                ),
                cellContent = CellContentDM(
                    cellValue = CellValue.fromAny(cellLiteralParser.parse(value)),
                    formula = formula
                )
            )

            val command = Commands.makeCommand(
                run = { updateCellAction.updateCellDM(request) },
                undo = { updateCellAction.updateCellDM(reverseRequest) }
            )
            stateCont.getWbState(wbKey)?.also {
                val cMs = it.commandStackMs
                cMs.value = cMs.value.add(command)
            }
            command.run()
            closeEditor()
        }
    }

    override fun closeEditor() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnCursor()
        }
        editorStateMs.value = editorState.clearAllText().close()
    }

    /**
     * **For testing only.**
     * Be careful when using this function. It directly updates the text content and will erase all the text formats. Should be use for testing only. Even so, be extra careful when use this in tests. Use [updateTextField] in the app instead.
     */
    @Deprecated("dont use, this one is not suitable for production")
    override fun updateText(newText: String) {
        val editorState by stateCont.cellEditorStateMs
        if (editorState.isActive) {
            stateCont.cellEditorStateMs.value = editorState
                .setCurrentText(newText)
//            val newTextField = editorState.currentTextField.copy(text=newText)
//            this.updateTextField(newTextField)
        }
    }

    /**
     * This function does the following:
     *  - update the text field displayed by the cell editor
     *  - point the new text to the correct location which could be either the current text, or the temp text, depending on the current state of the state editor. If the range selector is activated, the new text will be stored in the temp text, and only when the range selector is deactivated, the temp text is moved to the current text.
     *
     */
    override fun updateTextField(newTextField: TextFieldValue) {
        var editorState by stateCont.cellEditorStateMs
        var ntf = newTextField
        if (editorState.isActive) {
            val oldAllowRangeSelector = editorState.allowRangeSelector
            println(ntf)
            ntf = autoCompleteBracesIfPossible(ntf)

            val newEditorState = editorState
                .setCurrentTextField(ntf)
                .apply {
                    /*
                    when editor switch allow-range-selector flag
                    from true to false, move the respective cursor
                    to the currently edited cell
                    */
                    val from_Allow_To_Disallow: Boolean = oldAllowRangeSelector && !this.allowRangeSelector
                    if (editorState.rangeSelectorIsSameAsTargetCursor) {
                        if (from_Allow_To_Disallow) {
                            editorState.targetCursorId?.let { wbws ->
                                stateCont.getCursorStateMs(wbws)?.let { cursorStateMs ->
                                    editorState.targetCell?.let { editTarget ->
                                        cursorStateMs.value = cursorStateMs.value
                                            .removeAllExceptMainCell()
                                            .setMainCell(editTarget)
                                    }
                                }
                            }
                        }
                    }
                }
            editorState = newEditorState
        }
    }

    fun autoCompleteBracesIfPossible(newTextField: TextFieldValue): TextFieldValue {
        val oldTf = editorState.currentTextField
        val ntf = newTextField
        if(oldTf == ntf){
            return ntf
        }

        val diffRs = textDiffer.extractTextAddition(oldTf, newTextField)
        val rt = diffRs?.let { tnr ->
            val braces = when (tnr.text) {
                "(" -> "()"
                "[" -> "[]"
                "{" -> "{}"
                else -> null
            }
            val cursorPosition = tnr.range.end-1
            val newText: String = braces?.let {
                ntf.text.substring(0, cursorPosition) + it + ntf.text.substring(cursorPosition+1, ntf.text.length)
            } ?: ntf.text
            val newRange:TextRange = braces?.let {
                ntf.selection
            } ?: ntf.selection
            ntf.copy(text = newText, selection = newRange)
        } ?: ntf
        return rt
    }

    /**
     * pass keyboard event caught by a cell editor to its range-selector (which is a cell cursor).
     */
    private fun passKeyEventToRangeSelector(keyEvent: PKeyEvent, editorState: CellEditorState): Boolean {
        val rt: Boolean = editorState.rangeSelectorCursorId?.let {
            cursorAction.handleKeyboardEvent(keyEvent, it)
        } ?: false
        return rt
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(keyEvent: PKeyEvent): Boolean {
        if (editorState.isActive) {
            if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.key) {
                        Key.F4 ->{
                            cycleFormulaLockState()
                            return true
                        }
                        Key.Enter -> {
                            if (keyEvent.isAltPressedAlone) {
                                val currentText = editorState.currentTextField
                                val newText = currentText.copy(
                                    text = currentText.text + "\n",
                                    selection = TextRange(currentText.selection.end + 1)
                                )
                                updateTextField(newText)
                            } else {
                                runFormulaOrSaveValueToCell()
                            }
                            return true
                        }
                        Key.Escape -> {
                            closeEditor()
                            return true
                        }
                        else -> {
                            if (editorState.allowRangeSelector) {
                                if (keyEvent.isAcceptedByRangeSelector()) {
                                    val rt = passKeyEventToRangeSelector(keyEvent, editorState)
                                    if (keyEvent.isRangeSelectorNavKey()) {
                                        // x: generate rs text
                                        val rsText = makeDisplayText
                                            .makeRangeSelectorText(stateCont.cellEditorState)
                                        // x: update range selector text
                                        editorStateMs.value = editorState.setRangeSelectorText(rsText)
                                    }
                                    return rt
                                } else {
                                    editorStateMs.value = editorState.stopGettingRangeAddress()
                                    //propagate the key event further
                                    return false
                                }
                            } else {
                                //propagate the key event further
                                return false
                            }
                        }
                    }

            } else {
                return false
            }
        } else {
            return false
        }
    }
}
