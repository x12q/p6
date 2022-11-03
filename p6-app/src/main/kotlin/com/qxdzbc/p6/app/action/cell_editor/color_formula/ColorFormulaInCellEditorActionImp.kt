package com.qxdzbc.p6.app.action.cell_editor.color_formula

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.TextElementVisitor_Qualifier

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import javax.inject.Inject

class ColorFormulaInCellEditorActionImp @Inject constructor(
    val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    val formulaColorGenerator: FormulaColorGenerator,
    @TextElementVisitor_Qualifier
    val visitor: FormulaBaseVisitor<TextElementResult>,
): ColorFormulaInCellEditorAction {
    val sc by stateContSt
    override fun colorFormulaInCellEditor() {
         sc.cellEditorStateMs.value = colorFormulaInCellEditor(sc.cellEditorStateMs.value)
    }

    override fun colorFormulaInCellEditor(i: CellEditorState): CellEditorState {
        if (i.isActive) {
            val trailingSpace = i.currentTextField.text.let {
                val trailTrimmed = it.trimEnd()
                val diff = it.length - trailTrimmed.length
                if (diff > 0) {
                    " ".repeat(diff)
                } else {
                    ""
                }
            }
            val teRs = i.parseTree?.let {
                visitor.visit(it)
            }
            val crList: List<CellRangeElement>? = teRs?.cellRangeElements?.toSet()?.toList()
            val allElements = teRs?.allSortedWithPadding()
            if (allElements?.isNotEmpty() == true && crList?.isNotEmpty() == true) {
                val colors = formulaColorGenerator.getColors(crList.size)
                val newTextField: AnnotatedString = buildAnnotatedString {
                    for (e in allElements) {
                        when (e) {
                            is CellRangeElement -> {
                                val elementIndex = crList.indexOf(e)
                                val color = colors.getOrNull(elementIndex)
                                if (color != null) {
                                    withStyle(style = SpanStyle(color = color)) {
                                        append(e.text)
                                    }
                                } else {
                                    append(e.text)
                                }
                            }
                            else -> append(e.text)
                        }
                    }
                    append(trailingSpace)
                }
                val newTf = i.currentTextField.copy(
                    annotatedString = newTextField
                )
                return i.setCurrentTextField(newTf)
            } else {
                return i
            }
        } else {
            return i
        }
    }
}
