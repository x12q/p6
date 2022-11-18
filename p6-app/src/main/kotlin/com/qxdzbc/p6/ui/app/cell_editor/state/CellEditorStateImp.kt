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
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.translator.partial_text_element_extractor.PartialFormulaTreeExtractor
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.squareup.anvil.annotations.ContributesBinding
import org.antlr.v4.runtime.tree.ParseTree
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
data class CellEditorStateImp constructor(
    override val targetCursorIdSt: St<CursorStateId>? = null,
    override val isOpenMs: Ms<Boolean> = ms(false),
    override val currentTextField: TextFieldValue = TextFieldValue(""),
    override val targetCell: CellAddress? = null,
    override val rangeSelectorCursorIdSt: St<CursorStateId>? = null,
    override val rangeSelectorTextField: TextFieldValue? = null,
    override val parseTreeMs: Ms<ParseTree?> = ms(null),
    override val rangeSelectorAllowState: RangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
    override val rangeSelectorParseTreeMs: Ms<ParseTree?> = ms(null),
    val treeExtractor: TreeExtractor,
) : CellEditorState {

    @Inject
    constructor(
        @PartialTreeExtractor
        treeExtractor: TreeExtractor,
    ) : this(
        targetCursorIdSt = null,
        isOpenMs = ms(false),
        currentTextField = TextFieldValue(""),
        targetCell = null,
        rangeSelectorCursorIdSt = null,
        rangeSelectorTextField = null,
        parseTreeMs = ms(null),
        rangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
        treeExtractor = treeExtractor,
    )

    companion object {
        fun defaultForTest(): CellEditorStateImp {
            return CellEditorStateImp(PartialFormulaTreeExtractor())
        }
    }

    override val parseTree: ParseTree? get() = parseTreeMs.value
    private fun setParseTree(i: ParseTree?): CellEditorStateImp {
        this.parseTreeMs.value = i
        return this
    }

    override val rangeSelectorParseTree: ParseTree?
        get() = rangeSelectorParseTreeMs.value

    private fun setRangeSelectorParseTree(i: ParseTree?): CellEditorStateImp {
        this.rangeSelectorParseTreeMs.value = i
        return this
    }

    override val displayParseTreeMs: Ms<ParseTree?>
        get(){
            if(this.isOpen && this.allowRangeSelector){
                return this.rangeSelectorParseTreeMs
            }else{
                return this.parseTreeMs
            }
        }
    override val displayParseTree: ParseTree?
        get() {
            if(this.isOpen && this.allowRangeSelector){
                return this.rangeSelectorParseTree
            }else{
                return this.parseTree
            }
        }

    private fun setDisplayParseTree(i: ParseTree?): CellEditorStateImp {
        if(this.isOpen && this.allowRangeSelector){
            return this.setRangeSelectorParseTree(i)
        }else{
            return this.setParseTree(i)
        }
    }

    override fun clearAll(): CellEditorState {
        return this.clearAllText().setParseTree(null).setRangeSelectorParseTree(null)
    }

    override fun stopGettingRangeAddress(): CellEditorState {
        val rt = this.moveTextFromRangeSelectorTextToCurrentText()
        return rt
    }

    private fun moveTextFromRangeSelectorTextToCurrentText(): CellEditorState {
        if (rangeSelectorTextField != null) {
            return this.setCurrentText(rangeSelectorTextField.text).setRangeSelectorTextField(null)
        } else {
            return this
        }
    }

    override val allowRangeSelector: Boolean
        get() {
            return this.rangeSelectorAllowState.isAllow()
        }

    override val isOpen: Boolean by isOpenMs
    override val isNotOpen: Boolean
        get() = !isOpen
    override val isActiveAndAllowRangeSelector: Boolean
        get() = isOpen && allowRangeSelector
    override val currentText: String get() = currentTextField.text

    override val rangeSelectorCursorId: CursorStateId?
        get() = rangeSelectorCursorIdSt?.value

    override fun setRangeSelectorCursorId(i: St<CursorStateId>?): CellEditorState {
        return this.copy(rangeSelectorCursorIdSt = i)
    }

    override val targetCursorId: CursorStateId?
        get() = targetCursorIdSt?.value

    override fun setTargetCell(newCellAddress: CellAddress?): CellEditorState {
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
        val rt1=this.copy(rangeSelectorTextField = newTextField)
        val rt2 = newTextField?.text?.let{
            treeExtractor.extractTree(it)
        }?.map {
            rt1.setRangeSelectorParseTree(it)
        }?.component1() ?:rt1
        return rt2
    }

    override fun setCurrentText(newText: String): CellEditorStateImp {
        val newTf = this.currentTextField
            .copy(text = newText, selection = TextRange(newText.length))
        return this.setCurrentTextField(newTf)
    }

    override fun setCurrentTextField(newTextField: TextFieldValue): CellEditorStateImp {
        val oldTf = this.currentTextField
        val isTextCursorChanged =
            oldTf.text == newTextField.text && oldTf.selection != newTextField.selection
        val newRSAState = if (isTextCursorChanged) {
            this.rangeSelectorAllowState.transitWithMovingCursor(
                text = newTextField.text,
                selection = newTextField.selection,
            )
        } else {
            val newText = newTextField.text
            val textRange = newTextField.selection
            this.rangeSelectorAllowState.transitWithInput(
                text = newText,
                inputChar = newText.getOrNull(textRange.end - 1),
                inputIndex = textRange.end - 1
            )
        }

        val rt= if(newRSAState.isAllow()){
            this.setRangeSelectorTextField(newTextField)
        }else{
            this
        }.copy(currentTextField = newTextField, rangeSelectorAllowState = newRSAState)
        val rt2 = treeExtractor.extractTree(newTextField.text).map {
            rt.setParseTree(it)
        }.component1() ?: rt
        return rt2
    }

    override fun setDisplayTextField(newTextField: TextFieldValue): CellEditorState {
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
    override fun open(cursorIdMs: St<CursorStateId>): CellEditorState {
        isOpenMs.value = true
        val rsaState = if (this.currentText.isNotEmpty()) {
            RangeSelectorAllowState.DISALLOW
        } else {
            RangeSelectorAllowState.START
        }
        //TODO change range selector text base on the new rsa state
        val rt= if(rsaState.isAllow()){
//            this.setRangeSelectorTextField(this.currentTextField)
            this
        }else{
            this
        }.copy(
            targetCursorIdSt = cursorIdMs,
            rangeSelectorCursorIdSt = cursorIdMs,
            rangeSelectorAllowState = rsaState
        )
        return rt
    }

    override fun close(): CellEditorState {
        isOpenMs.value = false
        return this.copy(
            targetCursorIdSt = null,
            targetCell = null,
            rangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
        )
            .stopGettingRangeAddress()
            .setRangeSelectorTextField(null)
    }
}
