package com.qxdzbc.p6.ui.app.cell_editor.state

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.github.michaelbull.result.map
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.di.TextElementVisitorQ
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementResult
import com.qxdzbc.p6.translator.partial_text_element_extractor.TextElementVisitor
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDifferImp
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.squareup.anvil.annotations.ContributesBinding
import org.antlr.v4.runtime.tree.ParseTree
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
data class CellEditorStateImp (
    override val targetCursorIdSt: St<CursorId>? = null,
    override val isOpenMs: Ms<Boolean> = ms(false),
    override val currentTextField: TextFieldValue = TextFieldValue(""),
    override val targetCell: CellAddress? = null,
    override val rangeSelectorIdSt: St<CursorId>? = null,
    override val rangeSelectorTextField: TextFieldValue? = null,
    val currentParseTreeMs: Ms<ParseTree?> = ms(null),
    override val rangeSelectorAllowState: RangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
    val rangeSelectorParseTreeMs: Ms<ParseTree?> = ms(null),
    val treeExtractor: TreeExtractor,
    override val currentTextElementResult: TextElementResult? = null,
    override val rangeSelectorTextElementResult: TextElementResult? = null,
    val visitor: FormulaBaseVisitor<TextElementResult>,
    val textDiffer: TextDiffer,
) : CellEditorState {

    @Inject
    constructor(
        @PartialTreeExtractor
        treeExtractor: TreeExtractor,
        @TextElementVisitorQ
        visitor: FormulaBaseVisitor<TextElementResult>,
        textDiffer: TextDiffer,
    ) : this(
        targetCursorIdSt = null,
        isOpenMs = ms(false),
        currentTextField = TextFieldValue(""),
        targetCell = null,
        rangeSelectorIdSt = null,
        rangeSelectorTextField = null,
        currentParseTreeMs = ms(null),
        rangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
        treeExtractor = treeExtractor,
        visitor = visitor,
        textDiffer = textDiffer,
    )

    companion object {
        fun defaultForTest(): CellEditorStateImp {
            return CellEditorStateImp(PartialFormulaTreeExtractor(), TextElementVisitor(),TextDifferImp())
        }
    }

    override val displayTextElementResult: TextElementResult?
        get() {
            if (this.isOpen && this.allowRangeSelector) {
                return this.rangeSelectorTextElementResult
            } else {
                return this.currentTextElementResult
            }
        }

    override val currentParseTree: ParseTree? get() = currentParseTreeMs.value

    private fun setCurrentParseTree(i: ParseTree?): CellEditorStateImp {
        this.currentParseTreeMs.value = i
        return this
    }

    override val rangeSelectorParseTree: ParseTree?
        get() = rangeSelectorParseTreeMs.value

    private fun setRangeSelectorParseTree(i: ParseTree?): CellEditorStateImp {
        this.rangeSelectorParseTreeMs.value = i
        return this
    }

    val displayParseTreeMs: Ms<ParseTree?>
        get() {
            if (this.isOpen && this.allowRangeSelector) {
                return this.rangeSelectorParseTreeMs
            } else {
                return this.currentParseTreeMs
            }
        }

    override val displayParseTree: ParseTree?
        get() {
            if (this.isOpen && this.allowRangeSelector) {
                return this.rangeSelectorParseTree
            } else {
                return this.currentParseTree
            }
        }

    private fun setCurrentTextElementResult(i: TextElementResult?): CellEditorStateImp {
        return this.copy(currentTextElementResult = i)
    }

    private fun setRangeSelectorTextElementResult(i: TextElementResult?): CellEditorStateImp {
        return this.copy(rangeSelectorTextElementResult = i)
    }

    override fun clearAll(): CellEditorStateImp {
        return this.clearAllText()
            .setCurrentParseTree(null)
            .setRangeSelectorParseTree(null)
    }

    override fun completeGettingRangeAddress(): CellEditorStateImp {
        val rt = this.moveTextFromRangeSelectorTextToCurrentText()
        return rt
    }

    private fun moveTextFromRangeSelectorTextToCurrentText(): CellEditorStateImp {
        if (rangeSelectorTextField != null) {
            return this.setCurrentText(rangeSelectorTextField.text).setRangeSelectorTextField(null)
        } else {
            return this
        }
    }

    override val allowRangeSelector: Boolean
        get() {
            return this.rangeSelectorAllowState.isAllowedOrAllowMouse()
        }

    override val isOpen: Boolean by isOpenMs

    override val isNotOpen: Boolean
        get() = !isOpen

    override val isActiveAndAllowRangeSelector: Boolean
        get() = isOpen && allowRangeSelector

    override val currentText: String get() = currentTextField.text

    override val rangeSelectorId: CursorId?
        get() = rangeSelectorIdSt?.value

    override fun setRangeSelectorId(i: St<CursorId>?): CellEditorStateImp {
        return this.copy(rangeSelectorIdSt = i)
    }

    override val targetCursorId: CursorId?
        get() = targetCursorIdSt?.value

    override fun setTargetCell(newCellAddress: CellAddress?): CellEditorStateImp {
        return this.copy(targetCell = newCellAddress)
    }

    override val displayTextField: TextFieldValue
        get() {
            if (this.isOpen && this.allowRangeSelector) {
                val rst: TextFieldValue = this.rangeSelectorTextField ?: this.currentTextField
                return rst
            } else {
                return this.currentTextField
            }
        }

    override val displayText: String
        get() = displayTextField.text

    override val rangeSelectorText: String?
        get() = rangeSelectorTextField?.text

    override fun setRangeSelectorTextField(newTextField: TextFieldValue?): CellEditorStateImp {
        val ces1 = this.copy(rangeSelectorTextField = newTextField)
        val rt = if (newTextField?.text.isNullOrEmpty()) {
            ces1.setRangeSelectorParseTree(null).setRangeSelectorTextElementResult(null)
        } else {
            newTextField?.text?.let {
                treeExtractor.extractTree(it)
            }?.map {
                val ces2 = ces1.setRangeSelectorParseTree(it)
                val ces3 = visitor.visit(it)?.let {
                    ces2.setRangeSelectorTextElementResult(it)
                } ?: ces2
                ces3
            }?.component1() ?: ces1
        }

        return rt
    }

    override fun setCurrentText(newText: String): CellEditorStateImp {
        val newTf = this.currentTextField
            .copy(text = newText, selection = TextRange(newText.length))
        return this.setCurrentTextField(newTf)
    }

    /**
     * When set current text field. A couple of side effects will happen, including:
     * - range selector allow state will be re-evaluated
     * - base on range selector state, a new text will be generated
     */
    override fun setCurrentTextField(newTextField: TextFieldValue): CellEditorStateImp {

        // same text, just the text cursor has changed
        val onlyTextCursorChanged =
            currentTextField.text == newTextField.text && currentTextField.selection != newTextField.selection

        // compute new range selector state
        val newRangeSelectorState: RangeSelectorAllowState = if (onlyTextCursorChanged) {
            this.rangeSelectorAllowState.transitByMovingTextCursor(
                text = newTextField.text,
                selection = newTextField.selection,
            )
        } else {
            val newText = newTextField.text
            val textRange = newTextField.selection
//            val diffRs = textDiffer.runTextDif(currentTextField, newTextField)
//            when(diffRs){
//                is TextDiffResult.Addition -> {
//                    this.rangeSelectorAllowState.transitWithInput(
//                        text = newText,
//                        inputChar = diffRs.addition.text.firstOrNull(),
//                        inputIndex = diffRs.addition.range.start
//                    )
//                }
//                is TextDiffResult.Deletion -> {
//                    this.rangeSelectorAllowState.transit(
//                        text = newText,
//                        inputChar = null,
//                        inputIndex = null,
//                        textCursorIndex = null,
//                        moveTextCursorWithMouse = false,
//                        moveTextCursorWithKeyboard = false
//                    )
//                }
//
//                TextDiffResult.NoChange -> {
//                    this.rangeSelectorAllowState.transit(
//                        text=newText,
//                        inputChar = null,
//                        inputIndex = null,
//                        textCursorIndex = null,
//                        moveTextCursorWithMouse = false,
//                        moveTextCursorWithKeyboard = false,
//                    )
//                }
//            }
            val diffRs = textDiffer.extractTextAddition(currentTextField,newTextField)
            if(diffRs!=null){
                this.rangeSelectorAllowState.transitWithInput(
                    text = newText,
                    inputChar = newText.getOrNull(textRange.end - 1),
                    inputIndex = textRange.end - 1
                )
            }else{
                this.rangeSelectorAllowState.transit(
                    text = newText,
                    inputChar = null,
                    inputIndex = null,
                    textCursorIndex = null,
                    moveTextCursorWithMouse = false,
                    moveTextCursorWithKeyboard = false
                )
            }
        }

        val ces1 = if (newRangeSelectorState.isAllowedOrAllowMouse()) {
            this.setRangeSelectorTextField(newTextField)
        } else {
            this
        } .copy(currentTextField = newTextField, rangeSelectorAllowState = newRangeSelectorState)
//        return ces1
        // Update the parse tree, text element result reflecting the current formula
        val rt = if (newTextField.text.isEmpty()) {
            ces1.setCurrentParseTree(null)
                .setCurrentTextElementResult(null)
        } else {
            treeExtractor.extractTree(newTextField.text).map {
                val ces2 = ces1.setCurrentParseTree(it)
                val ces3 = visitor.visit(it)?.let {textElementRs->
                    ces2.setCurrentTextElementResult(textElementRs)
                } ?: ces2
                ces3
            }.component1() ?: ces1
        }

        return rt
    }

    override fun setDisplayTextField(newTextField: TextFieldValue): CellEditorStateImp {
        if (this.isOpen && allowRangeSelector) {
            return this.setRangeSelectorTextField(newTextField)
        } else {
            return this.setCurrentTextField(newTextField)
        }
    }

    override fun clearAllText(): CellEditorStateImp {
        return this.setCurrentText("").setRangeSelectorTextField(null)
    }

    /**
     * Open this cell editor at the cursor whose id is [cursorIdMs]
     */
    override fun open(cursorIdMs: St<CursorId>): CellEditorStateImp {
        isOpenMs.value = true
        val rsaState = if (this.currentText.isNotEmpty()) {
            RangeSelectorAllowState.DISALLOW
        } else {
            RangeSelectorAllowState.START
        }

        val rt = if (rsaState.isAllowedOrAllowMouse()) {
            //x: set range selector text base on the new rsa state
            //x: this happens when the formula is errors and has a trailing activation char
            this.setRangeSelectorTextField(this.currentTextField)
        } else {
            this
        }.copy(
            targetCursorIdSt = cursorIdMs,
            rangeSelectorIdSt = cursorIdMs,
            rangeSelectorAllowState = rsaState
        )
        return rt
    }

    override fun close(): CellEditorStateImp {
        isOpenMs.value = false
        return this
            .copy(
                targetCursorIdSt = null,
                targetCell = null,
                rangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
            )
            .completeGettingRangeAddress()
            .clearAll()
    }
}
