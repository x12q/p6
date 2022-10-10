package com.qxdzbc.p6.ui.window.formula_bar

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.qxdzbc.p6.ui.common.color_generator.ColorMapImp
import com.qxdzbc.p6.ui.window.state.WindowState

class FormulaBarStateImp(
    private val windowState:WindowState
) : FormulaBarState {
    override val text: String
        get() {
            val wsState = windowState.activeWbState?.activeSheetState
            if(wsState!=null){
                val cellAddress = wsState.cursorState.mainCell
                val cell = wsState.worksheet.getCell(cellAddress)
                return cell?.editableValue(wsState.wbKey, wsState.name) ?: ""
            }else{
                return ""
            }
        }
    override val annotatedText: AnnotatedString
        get() {
            val wsState = windowState.activeWbState?.activeSheetState
            if(wsState!=null){
                val cellAddress = wsState.cursorState.mainCell
                val cell = wsState.worksheet.getCell(cellAddress)

                if(cell!=null){
                    val cellRangeExUnits = cell.content.exUnit?.getCellRangeExUnit()?.toSet()?.toList() ?: emptyList()
                    val colorMap = ColorMapImp(
                        colorKeys = cellRangeExUnits,
                        colors =  windowState.formulaColorGenerator.getColors(cellRangeExUnits.size)
                    )
                    return cell.colorEditableValue(colorMap,wsState.wbKey, wsState.name)
                }else{
                    return  AnnotatedString ("")
                }

            }else{
                return buildAnnotatedString { append("") }
            }
        }
}
