package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.PartialCellRangeExtractorQ
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CycleFormulaLockStateImp @Inject constructor(
    val stateCont:StateContainer,
    @PartialCellRangeExtractorQ
    val partialTextElementExtractor: P6Translator<TextElementResult>,
) : CycleFormulaLockStateAction {

    val sc  = stateCont

    override fun cycleFormulaLockState() {
        val ces by sc.cellEditorStateMs
        val currentTextField = ces.currentTextField
        val currentCursorPosition = currentTextField.selection.end
        val cellRangeLocList = ces.displayTextElementResult?.cellRangeElements
        if (cellRangeLocList?.isNotEmpty() == true) {
            val formula = currentTextField.text
            val newText = this.cycleFormulaLockState(formula, cellRangeLocList, currentCursorPosition)
            if (newText != null) {
                val newTextField = currentTextField.copy(
                    text = newText
                )
                sc.cellEditorStateMs.value = ces.setCurrentTextField(newTextField)
            }
        }
    }

    override fun cycleFormulaLockState(
        formula: String,
        cellRangePosList: List<CellRangeElement>,
        cursorPos: Int
    ): String? {
        val rt = cellRangePosList.let {
            val cellRangePos: CellRangeElement? = cellRangePosList
                .firstOrNull { cursorPos in (it.startTP.charIndex..it.stopTP.charIndex + 1) }
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
                    newRangeAddress.topLeft.label
                } else {
                    newRangeAddress.label
                } + (cellRangePos.cellRangeSuffix ?: "")
                formula.replaceRange(cellRangePos.iRange(), newLabel)
            }
        }
        return rt
    }

    override fun cycleFormulaLockState(formula: String, cursorPos: Int): String? {
        val rt = partialTextElementExtractor.translate(formula).component1()?.let { rs ->
            cycleFormulaLockState(formula, rs.cellRangeElements, cursorPos)
        }
        return rt
    }
}
