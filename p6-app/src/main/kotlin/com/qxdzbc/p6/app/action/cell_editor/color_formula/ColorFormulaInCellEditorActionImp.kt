package com.qxdzbc.p6.app.action.cell_editor.color_formula

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common.BuildAnnotatedTextAction
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.TextElementVisitorQ
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TextElement
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class ColorFormulaInCellEditorActionImp @Inject constructor(
    val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    val formulaColorGenerator: FormulaColorGenerator,
    @TextElementVisitorQ
    val visitor: FormulaBaseVisitor<TextElementResult>,
    val buildAnnotatedTextAction: BuildAnnotatedTextAction,
) : ColorFormulaInCellEditorAction {

    val sc by stateContSt

    override fun colorCurrentTextInCellEditor() {
        sc.cellEditorStateMs.value = colorCurrentTextInCellEditor(sc.cellEditorStateMs.value)
    }

    override fun colorCurrentTextInCellEditor(cellEditorState: CellEditorState): CellEditorState {
        val ces = cellEditorState
        if (ces.isOpen) {
            val teRs: TextElementResult? = ces.parseTree?.let {
                visitor.visit(it)
            }
            val creList: List<CellRangeElement>? = teRs?.cellRangeElements?.toSet()?.toList()
            val allElements = teRs?.all
            if(allElements?.isNotEmpty() == true&& creList?.isNotEmpty()==true){
                val trailingSpace: String = computeTrailingSpace(ces.currentText)
                val newTextField=makeAnnotatedString(trailingSpace,allElements, creList)
                                val newTf = ces.currentTextField.copy(
                    annotatedString = newTextField
                )
                return ces.setCurrentTextField(newTf)
            }else{
                return ces
            }
        } else {
            return ces
        }
    }

    private fun computeTrailingSpace(text: String): String {
        val trailTrimmed = text.trimEnd()
        val diff = text.length - trailTrimmed.length
        if (diff > 0) {
            return " ".repeat(diff)
        } else {
            return ""
        }
    }

    private fun makeAnnotatedString(
        trailingSpace: String,
        allElements: List<TextElement>,
        creList: List<CellRangeElement>
    ): AnnotatedString {
        val newText=if (allElements.isNotEmpty() && creList.isNotEmpty()) {
            val colors = formulaColorGenerator.getColors(creList.size)
            val newAnnotatedStr: AnnotatedString = buildAnnotatedString {
                append(
                    buildAnnotatedTextAction.buildAnnotatedText(allElements, colors.map { SpanStyle(color = it) })
                )
                append(trailingSpace)
            }
            newAnnotatedStr
        }else{
            AnnotatedString("")
        }
        return newText
    }

    override fun colorDisplayTextInCellEditor(cellEditorState: CellEditorState): CellEditorState {
        val ces = cellEditorState
        if (ces.isOpen) {
            val teRs: TextElementResult? = ces.parseTree?.let {
                visitor.visit(it)
            }
            val creList: List<CellRangeElement>? = teRs?.cellRangeElements?.toSet()?.toList()
            val allElements = teRs?.all
            if(allElements?.isNotEmpty()==true && creList?.isNotEmpty()==true){
                val trailingSpace: String = computeTrailingSpace(ces.displayText)
                val newText=makeAnnotatedString(trailingSpace,allElements, creList)
                val newTf = ces.displayTextField.copy(annotatedString = newText)
                return ces.setDisplayTextField(newTf)
            }else{
                return ces
            }
        } else {
            return ces
        }
    }
}

