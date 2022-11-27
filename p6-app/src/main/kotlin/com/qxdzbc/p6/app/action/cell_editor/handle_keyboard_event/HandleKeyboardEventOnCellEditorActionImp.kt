package com.qxdzbc.p6.app.action.cell_editor.handle_keyboard_event

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.text.TextRange
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer
import com.qxdzbc.p6.ui.app.state.StateContainer

class HandleKeyboardEventOnCellEditorActionImp(
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
) : HandleKeyboardEventOnCellEditorAction {
    private val stateCont by stateContMs
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(keyEvent: P6KeyEvent): Boolean {
        TODO()
//        if (editorState.isOpen) {
//            if (keyEvent.type == KeyEventType.KeyDown) {
//                when (keyEvent.key) {
//                    Key.F4 -> {
//                        cycleLockStateAct.cycleFormulaLockState()
//                        return true
//                    }
//
//                    Key.Enter -> {
//                        if (keyEvent.isAltPressedAlone) {
//                            val newText = editorState.currentTextField
//                                .let { ctf ->
//                                    ctf.copy(
//                                        text = ctf.text + "\n",
//                                        selection = TextRange(ctf.selection.end + 1)
//                                    )
//                                }
//                            changeTextField(newText)
//                        } else {
//                            runFormulaOrSaveValueToCell()
//                        }
//                        return true
//                    }
//
//                    Key.Escape -> {
//                        closeEditor()
//                        return true
//                    }
//
//                    else -> {
////                        if (editorState.allowRangeSelector) {
//                        if (editorState.rangeSelectorAllowState == RangeSelectorAllowState.ALLOW) {
//                            if (keyEvent.isAcceptedByRangeSelector()) {
//                                val rt = this.passKeyEventToRangeSelector(keyEvent, editorState.rangeSelectorCursorId)
//                                if (keyEvent.isRangeSelectorNavKey()) {
//                                    // x: generate range selector text
//                                    val rsText = makeDisplayText
//                                        .makeRangeSelectorText(stateCont.cellEditorState)
//                                    // x: update range selector text
//                                    editorStateMs.value =
//                                        colorFormulaAction.colorDisplayTextInCellEditor(
//                                            editorState.setRangeSelectorTextField(rsText)
//                                        )
//                                }
//                                return rt
//                            } else {
//                                editorStateMs.value = editorState.stopGettingRangeAddress()
//                                // propagate the key event further
//                                return false
//                            }
//                        } else {
//                            // propagate the key event further
//                            return false
//                        }
//                    }
//                }
//
//            } else {
//                return false
//            }
//        } else {
//            return false
//        }
    }
}
