package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.CellRangeExtractor_Qualifier
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.translator.cell_range_extractor.CellRangeExtractor
import com.qxdzbc.p6.translator.cell_range_extractor.CellRangePosition
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class CycleFormulaLockStateImp @Inject constructor(
    @StateContainerSt
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    @CellRangeExtractor_Qualifier
    val cellRangeExtractor: CellRangeExtractor
) : CycleFormulaLockStateAction {

    val sc by stateContSt

    override fun cycleFormulaLockState() {
        val editorState by sc.cellEditorStateMs
        val currentTextField = editorState.currentTextField
        val currentCursorPosition = currentTextField.selection.end
        val newText = this.cycleFormulaLockState(currentTextField.text, currentCursorPosition)
        if (newText != null) {
            val newTextField = currentTextField.copy(
                text = newText
            )
            sc.cellEditorStateMs.value = editorState.setCurrentTextField(newTextField)
        }
    }

    override fun cycleFormulaLockState(formula: String, cursorPos: Int): String? {
        val rt = cellRangeExtractor.translate(formula).component1()?.let { lst ->
            val cellRangePos: CellRangePosition? = lst
                .firstOrNull { cursorPos in (it.start.charIndex..it.stop.charIndex + 1) }
            cellRangePos?.let {
                val label = it.cellRangeLabel
                if (label.contains(":")) {
                    RangeAddress(label)
                } else {
                    RangeAddress(CellAddress(label))
                }
            }?.let {
                val newRangeAddress = it.nextLockState()
                val newLabel = if (newRangeAddress.isCell()) {
                    newRangeAddress.topLeft.toLabel()
                } else {
                    newRangeAddress.label
                } + (cellRangePos.labelLoc?:"")
                formula.replaceRange(cellRangePos.iRange(), newLabel)
            }
        }
        return rt
    }
}
