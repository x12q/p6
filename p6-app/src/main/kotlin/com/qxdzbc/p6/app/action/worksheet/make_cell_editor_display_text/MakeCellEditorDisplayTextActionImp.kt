package com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.di.state.app_state.SubAppStateContainerSt
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import javax.inject.Inject

class MakeCellEditorDisplayTextActionImp @Inject constructor(
    @SubAppStateContainerSt
    val stateContSt:St<@JvmSuppressWildcards SubAppStateContainer>
) : MakeCellEditorDisplayTextAction {

    private val stateCont by stateContSt

    override fun makeRangeSelectorText(editorState: CellEditorState): TextFieldValue {
        if(editorState.allowRangeSelector){
            val rangeSelector:CursorState? = editorState.rangeSelectorCursorId?.let {
                stateCont.getCursorState(it)
            }
            val wsName:String? = rangeSelector?.wsName
            val rangeAddress:RangeAddress? = rangeSelector?.let { getSelectedRange(it) }
            val rangeSelectorWbKey:WorkbookKey? = rangeSelector?.wbKey
            if(wsName!=null && rangeAddress!=null && rangeSelectorWbKey!=null){
                val rangeStr:String = if(rangeAddress.isCell()){
                    rangeAddress.topLeft.toRawLabel()
                }else{
                    rangeAddress.rawLabel
                }
                val isSameCursor:Boolean = editorState.targetCursorId == editorState.rangeSelectorCursorId
                val selectedRangeText:String =if(isSameCursor){
                    rangeStr
                }else{
                    val sameWb = rangeSelectorWbKey == editorState.targetWbKey
                    if(sameWb){
                        "${rangeStr}@${wsName}"
                    }else{
                        val path = rangeSelectorWbKey.path
                        "${rangeStr}@${wsName}@${rangeSelectorWbKey.name}"+ if(path!=null) "@${path.toAbsolutePath()}" else ""
                    }
                }
                val currentText=editorState.currentTextField.text
                val newText = currentText+selectedRangeText
                return editorState.currentTextField.copy(text=newText,selection = TextRange(newText.length))
            }else{
                return editorState.currentTextField
            }
        }else{
            return editorState.currentTextField
        }
    }
    fun getSelectedRange(cursorState:CursorState):RangeAddress{
        val mainRange =cursorState.mainRange
        if(mainRange!=null) {
            return mainRange
        }else{
            val mainCell = cursorState.mainCell
            return RangeAddress(mainCell)
        }
    }
}
