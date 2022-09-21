package com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.qxdzbc.common.compose.key_event.PKeyEvent
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest2
import com.qxdzbc.p6.app.document.cell.d.CellValue
import com.qxdzbc.p6.di.state.app_state.StateContainerMs
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.ui.document.cell.action.UpdateCellAction
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

class CellEditorActionImp @Inject constructor(
    private val cellLiteralParser: CellLiteralParser,
    private val updateCellAction: UpdateCellAction,
    private val cursorAction: CursorAction,
    private val makeDisplayText: MakeCellEditorDisplayText,
    private val openCellEditor: OpenCellEditorAction,
    @StateContainerMs
    private val stateContMs:Ms<StateContainer>
) : CellEditorAction,
    MakeCellEditorDisplayText by makeDisplayText,
    OpenCellEditorAction by openCellEditor
{

    private var stateCont by stateContMs
    var editorState by stateCont.cellEditorStateMs

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

    private fun getWsState(editorState: CellEditorState): Ms<WorksheetState>? {
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        if (wbKey != null && wsName != null) {
            return stateCont.getWsStateMs(wbKey, wsName)
        } else {
            return null
        }
    }

    override fun runFormula() {
        val editorState by stateCont.cellEditorStateMs
        val wsStateMs = getWsState(editorState)
        val ws = wsStateMs?.value?.worksheet
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        val editTarget = editorState.targetCell
        if (ws != null && wbKey != null && wsName != null && editTarget != null) {
            // x: execute the formula in the editor
            val cell = ws.getCell(editTarget)
            val codeText = editorState.rangeSelectorTextField?.text ?: editorState.currentText

            val reverseRequest = if (cell?.fullFormula != null) {
//                CellUpdateRequest(
//                    wbKey = wbKey,
//                    wsName = wsName,
//                    cellAddress = editTarget,
//                    formula = cell.formula,
//                )
                CellUpdateRequest2(
                    wbKey = wbKey,
                    wsName = wsName,
                    cellAddress = editTarget,
                    cellContent = CellContentDM.fromFormula(cell.fullFormula)
                )
            } else {
//                CellUpdateRequest(
//                    wbKey = wbKey,
//                    wsName = wsName,
//                    cellAddress = editTarget,
//                    cellValue = cell?.currentValue,
//                )
                CellUpdateRequest2(
                    wbKey = wbKey,
                    wsName = wsName,
                    cellAddress = editTarget,
                    cellContent= CellContentDM.fromAny(cell?.currentValue)
                )
            }
            var value: String? = null
            var formula: String? = null
            if (isFormula(codeText)) {
                formula = codeText
            } else {
                value = codeText
            }
            val request = CellUpdateRequest2(
                wbKey = wbKey,
                wsName = wsName,
                cellAddress = editTarget,
                cellContent = CellContentDM(
                    cellValue = CellValue.fromAny(cellLiteralParser.parse(value)),
                    formula = formula
                )
            )

            val command = Commands.makeCommand(
                run = { updateCellAction.updateCell2(request) },
                undo = { updateCellAction.updateCell2(reverseRequest) }
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
        editorState = editorState.clearAllText().close()
    }

    override fun updateText(newText: String, ) {
        var editorState by stateCont.cellEditorStateMs
        if (editorState.isActive) {
            editorState = editorState
                .setCurrentText(newText)
        }
    }

    override fun updateTextField(newTextField: TextFieldValue) {
        var editorState by stateCont.cellEditorStateMs
        if (editorState.isActive) {
            val oldAllowRangeSelector = editorState.allowRangeSelector
            val newEditorState = editorState
                .setCurrentTextField(newTextField)
                .apply {
                    /*
                    when editor switch allow-range-selector flag
                    from true to false, move the respective cursor
                    to the currently edited cell
                    */
                    val isFromAllow_To_Disallow: Boolean = oldAllowRangeSelector && !this.allowRangeSelector
                    if (editorState.rangeSelectorIsSameAsTargetCursor) {
                        if (isFromAllow_To_Disallow) {
                            editorState.targetWbWs?.let { wbws ->
                                stateCont.getCursorStateMs(wbws)?.let { cursorStateMs ->
                                    editorState.targetCell?.let { editTarget ->
                                        cursorStateMs.value = cursorStateMs.value
                                            .removeAllExceptAnchorCell()
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

    fun passKeyEventToRangeSelector(keyEvent: PKeyEvent, editorState: CellEditorState): Boolean {
        val wbws = editorState.rangeSelectorCursorId
        if (wbws != null) {
            val rt = stateCont.getCursorState(wbws)?.let { cs: CursorState ->
                cursorAction.handleKeyboardEvent(keyEvent, cs)
            } ?: false
            return rt
        } else {
            return false
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(keyEvent: PKeyEvent): Boolean {
        if (editorState.isActive) {
            when (keyEvent.key) {
                Key.Enter -> {
                    runFormula()
                    return true
                }
                Key.Escape -> {
                    closeEditor()
                    return true
                }
                else -> {
                    if (editorState.allowRangeSelector) {
                        if (keyEvent.isRangeSelectorToleratedKey()) {
                            val rt = passKeyEventToRangeSelector(keyEvent, editorState)
                            if(keyEvent.isRangeSelectorNavKey()){
                                // x: generate rs text
                                val rsText = makeDisplayText
                                    .makeRangeSelectorText(stateCont.cellEditorState)
                                // x: update range selector text
                                editorState = editorState.setRangeSelectorText(rsText)
                            }
                            return  rt
                        }else{
                            editorState = editorState.stopGettingRangeAddress()
                            return false //propagate the key event to the editor text field
                        }
                    } else {
                        return false //propagate the key event to the editor text field
                    }
                }
            }
        } else {
            return false
        }
    }
}
