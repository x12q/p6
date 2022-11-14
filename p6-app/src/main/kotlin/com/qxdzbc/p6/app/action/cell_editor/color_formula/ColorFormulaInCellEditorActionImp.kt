package com.qxdzbc.p6.app.action.cell_editor.color_formula

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common.BuildAnnotatedTextAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.TextElementVisitorQ
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ColorFormulaInCellEditorActionImp @Inject constructor(
    val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    val formulaColorGenerator: FormulaColorGenerator,
    @TextElementVisitorQ
    val visitor: FormulaBaseVisitor<TextElementResult>,
    val buildAnnotatedTextAction: BuildAnnotatedTextAction,
): ColorFormulaInCellEditorAction {

    val sc by stateContSt

    override fun formatCurrentFormulaInCellEditor() {
         sc.cellEditorStateMs.value = formatFormulaInCellEditor(sc.cellEditorStateMs.value)
    }

    override fun formatFormulaInCellEditor(i: CellEditorState): CellEditorState {
        if (i.isOpen){
            val trailingSpace:String = i.currentText.let {
                val trailTrimmed = it.trimEnd()
                val diff = it.length - trailTrimmed.length
                if (diff > 0) {
                    " ".repeat(diff)
                } else {
                    ""
                }
            }
            val teRs:TextElementResult? = i.parseTree?.let {
                visitor.visit(it)
            }
            val creList: List<CellRangeElement>? = teRs?.cellRangeElements?.toSet()?.toList()
            val allElements = teRs?.all
            if (allElements?.isNotEmpty() == true && creList?.isNotEmpty() == true) {
                val colors = formulaColorGenerator.getColors(creList.size)
                val newTextField: AnnotatedString = buildAnnotatedString {
                    append(
                        buildAnnotatedTextAction.buildAnnotatedText(allElements,colors.map { SpanStyle(color = it) })
                    )
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
