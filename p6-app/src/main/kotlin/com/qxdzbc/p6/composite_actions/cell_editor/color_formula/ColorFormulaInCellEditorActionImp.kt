package com.qxdzbc.p6.composite_actions.cell_editor.color_formula

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.qxdzbc.p6.composite_actions.common.BuildAnnotatedTextAction
import com.qxdzbc.p6.di.P6AnvilScope

import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.CellRangeElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TextElement
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorGenerator
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class ColorFormulaInCellEditorActionImp @Inject constructor(
    val stateCont:StateContainer,
    private val formulaColorGenerator: FormulaColorGenerator,
    private val buildAnnotatedTextAction: BuildAnnotatedTextAction,
) : ColorFormulaInCellEditorAction {

    val sc  = stateCont

    override fun colorCurrentTextInCellEditor() {
        sc.cellEditorStateMs.value = colorCurrentTextInCellEditor(sc.cellEditorStateMs.value)
    }

    /**
     * TODO This one is generating malformed formula while the underlying formula is correct.
     */
    override fun colorCurrentTextInCellEditor(cellEditorState: CellEditorState): CellEditorState {
        val ces = cellEditorState
        if (ces.isOpen) {
            val teRs: TextElementResult? = ces.displayTextElementResult
            val creList: List<CellRangeElement>? = teRs?.cellRangeElements?.toSet()?.toList()
            val allElements = teRs?.all
            if(allElements?.isNotEmpty() == true&& creList?.isNotEmpty()==true){
                val trailingSpace: String = extractTrailingSpace(ces.currentText)
                val newTextField=makeAnnotatedString(trailingSpace,allElements, creList)
                val newTf = ces.displayTextField.copy(annotatedString = newTextField)
                return ces.setDisplayTextField(newTf)
            }else{
                return ces
            }
        } else {
            return ces
        }
    }

    override fun colorDisplayTextInCellEditor(cellEditorState: CellEditorState): CellEditorState {
        val ces = cellEditorState
        if (ces.isOpen) {
            val teRs: TextElementResult? = ces.displayTextElementResult
            val creList: List<CellRangeElement>? = teRs?.cellRangeElements?.toSet()?.toList()
            val allElements = teRs?.all
            if(allElements?.isNotEmpty()==true && creList?.isNotEmpty()==true){
                val trailingSpace: String = extractTrailingSpace(ces.displayText)
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


    /**
     * extract the trailing space of a [text] if it has any.
     */
    fun extractTrailingSpace(text: String): String {
        val trailTrimmed = text.trimEnd()
        val diff = text.length - trailTrimmed.length
        if (diff > 0) {
            return " ".repeat(diff)
        } else {
            return ""
        }
    }

    fun makeAnnotatedString(
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
}

