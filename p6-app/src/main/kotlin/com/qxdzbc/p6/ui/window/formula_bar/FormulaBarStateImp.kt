package com.qxdzbc.p6.ui.window.formula_bar

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.qxdzbc.p6.ui.window.state.WindowState

class FormulaBarStateImp(
    private val windowState:WindowState
) : FormulaBarState {
    override val text: String
        get() {
            val wsState = windowState.activeWorkbookState?.activeSheetState
            if(wsState!=null){
                val cellAddress = wsState.cursorState.mainCell
                val cell = wsState.worksheet.getCellOrNull(cellAddress)
                return cell?.editableValue(wsState.wbKey, wsState.name) ?: ""
            }else{
                return ""
            }
        }
    override val annotatedText: AnnotatedString
        get() {
            val wsState = windowState.activeWorkbookState?.activeSheetState
            if(wsState!=null){
                val cellAddress = wsState.cursorState.mainCell
                val cell = wsState.worksheet.getCellOrNull(cellAddress)
                return cell?.colorEditableValue(windowState.colorProvider,wsState.wbKey, wsState.name) ?: buildAnnotatedString { append("") }
            }else{
                return buildAnnotatedString { append("") }
            }
        }
}
