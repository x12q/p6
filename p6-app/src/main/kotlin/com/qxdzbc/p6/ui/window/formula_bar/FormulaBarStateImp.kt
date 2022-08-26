package com.qxdzbc.p6.ui.window.formula_bar

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
                return cell?.editableValue?:""
            }else{
                return ""
            }
        }
}
