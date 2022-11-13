package com.qxdzbc.p6.ui.app.cell_editor.actions

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.github.michaelbull.result.mapBoth
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = CellEditorAction::class)
class CellEditorActionImp @Inject constructor(
    private val cellLiteralParser: CellLiteralParser,
    private val updateCellAction: UpdateCellAction,
    private val handleCursorKeyboardEventAct: HandleCursorKeyboardEventAction,
    private val makeDisplayText: MakeCellEditorTextAction,
    private val openCellEditor: OpenCellEditorAction,
    private val stateContMs: Ms<StateContainer>,
    private val textDiffer: TextDiffer,
    val cycleLockStateAct: CycleFormulaLockStateAction,
    @PartialTreeExtractor
    val treeExtractor: TreeExtractor,
    val colorFormulaAction: ColorFormulaInCellEditorAction
) : CellEditorAction,
    CycleFormulaLockStateAction by cycleLockStateAct,
    MakeCellEditorTextAction by makeDisplayText,
    OpenCellEditorAction by openCellEditor {

    private val stateCont by stateContMs
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs

    private fun isFormula(formula: String): Boolean {
        val script: String = formula.trim()
        val isFormula: Boolean = script.startsWith("=")
        return isFormula
    }

    override fun focusOnCellEditor() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnEditor()
        }
    }

    override fun freeFocusOnCellEditor() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.freeFocusOnEditor()
        }
    }

    override fun setCellEditorFocus(i: Boolean) {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.setCellEditorFocus(i)
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
        editorStateMs.value = editorState.clearAll().close()
    }

    /**
     * **For testing only.**
     * Be careful when using this function. It directly updates the text content and will erase all the text formats. Should be use for testing only. Even so, be extra careful when use this in tests. Use [changeText] in the app instead.
     */
    override fun changeText(newText: String) {
        val editorState by stateCont.cellEditorStateMs
        if (editorState.isOpen) {
            val newTextField = TextFieldValue(text = newText, selection = TextRange(newText.length))
            this.changeTextField(newTextField)
        }
    }

    /**
     * This function is called when users type with their keyboards into the editor. It does the following:
     *  - update the text field displayed by the cell editor
     *  - point the new text to the correct location which could be either the current text, or the temp text, depending on the current state of the state editor. If the range selector is activated, the new text will be stored in the temp text, and only when the range selector is deactivated, the temp text is moved to the current text.
     *  - update the internal parse tree of cell editor
     *
     */
    override fun changeTextField(newTextField: TextFieldValue) {
        val editorState by stateCont.cellEditorStateMs
        var ntf = newTextField
        if (editorState.isOpen) {
            val oldAllowRangeSelector = editorState.allowRangeSelector
            ntf = autoCompleteBracesIfPossible(ntf)

            val newEditorState = editorState
                .setCurrentTextField(ntf)
                .apply {
                    /*
                    when the cell editor switches off allow-range-selector flag, if the range-selector cursor and target cursor are the same, reset the cursor state and point it to the currently edited cell so that on the UI it moves back to the currently edited cell. This does not change the content of the cell editor state.
                    */
                    val from_Allow_To_Disallow: Boolean = oldAllowRangeSelector && !this.allowRangeSelector
                    if (from_Allow_To_Disallow) {
                        if (editorState.rangeSelectorIsSameAsTargetCursor) {
                            editorState.targetCursorId?.let { cursorId ->
                                stateCont.getCursorStateMs(cursorId)
                            }?.let { targetCursorMs ->
                                editorState.targetCell?.let { targetCell ->
                                    targetCursorMs.value = targetCursorMs.value
                                        .removeAllExceptMainCell()
                                        .setMainCell(targetCell)
                                }
                            }
                        }
                    }
                }.let { cellEditor ->
                    // update the parse tree inside the cell editor
                    val newCE = treeExtractor.extractTree(cellEditor.currentText)
                        .mapBoth(
                            success = {
                                cellEditor.setParseTree(it)
                            },
                            failure = {
                                cellEditor
                            }
                        )
                    newCE
                }
            stateCont.cellEditorStateMs.value = newEditorState
            colorFormulaAction.formatCurrentFormulaInCellEditor()
        }
    }

    fun autoCompleteBracesIfPossible(newTextField: TextFieldValue): TextFieldValue {
        val oldTf = editorState.currentTextField
        val ntf = newTextField
        if (oldTf == ntf) {
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
            val cursorPosition = tnr.range.end - 1
            val newText: String = braces?.let {
                ntf.text.substring(0, cursorPosition) + it + ntf.text.substring(cursorPosition + 1, ntf.text.length)
            } ?: ntf.text
            val newRange: TextRange = braces?.let {
                ntf.selection
            } ?: ntf.selection
            ntf.copy(text = newText, selection = newRange)
        } ?: ntf
        return rt
    }

    /**
     * pass keyboard event caught by a cell editor to its range selector (which is a cell cursor).
     */
    private fun passKeyEventToRangeSelector(keyEvent: P6KeyEvent, rangeSelectorId: CursorStateId?): Boolean {
        val rt: Boolean = rangeSelectorId?.let {
            handleCursorKeyboardEventAct.handleKeyboardEvent(keyEvent, it)
        } ?: false
        return rt
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(keyEvent: P6KeyEvent): Boolean {
        if (editorState.isOpen) {
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.F4 -> {
                        cycleFormulaLockState()
                        return true
                    }

                    Key.Enter -> {
                        if (keyEvent.isAltPressedAlone) {
                            val newText = editorState.currentTextField
                                .let { ctf ->
                                    ctf.copy(
                                        text = ctf.text + "\n",
                                        selection = TextRange(ctf.selection.end + 1)
                                )
                            }
                            changeTextField(newText)
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
                                val rt = this.passKeyEventToRangeSelector(keyEvent, editorState.rangeSelectorCursorId)
                                if (keyEvent.isRangeSelectorNavKey()) {
                                    // x: generate range selector text
                                    val rsText = makeDisplayText
                                        .makeRangeSelectorText(stateCont.cellEditorState)
                                    // x: update range selector text
                                    editorStateMs.value = editorState.setRangeSelectorTextField(rsText)
                                }
                                return rt
                            } else {
                                editorStateMs.value = editorState.stopGettingRangeAddress()
                                // propagate the key event further
                                return false
                            }
                        } else {
                            // propagate the key event further
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
