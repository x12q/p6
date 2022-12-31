package com.qxdzbc.p6.app.action.cell_editor.color_formula

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import kotlin.test.*
import test.BaseAppStateTest

internal class ColorFormulaInCellEditorActionImpTest : BaseAppStateTest(){
    lateinit var act:ColorFormulaInCellEditorActionImp
    lateinit var cellEditorAct:CellEditorAction
    lateinit var wbwsSt:WbWsSt
    val cellEditorState get()=ts.sc.cellEditorState
    @BeforeTest
    override fun _b() {
        super._b()
        act = ts.comp.colorFormulaActionImp()
        cellEditorAct = ts.comp.cellEditorAction()
        wbwsSt = ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!
    }
    @Test
    fun `colorFormula preserve text content`(){
        val wbwsSt = ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!
        cellEditorAct.openCellEditor(wbwsSt)
        cellEditorAct.changeText("=F1")

        assertEquals("=F1",ts.sc.cellEditorState.currentText)

        val t3 = "=A1+B2+C2"
        cellEditorAct.changeText(t3)
        val currentText = cellEditorState.currentTextField.text
        val newState=act.colorCurrentTextInCellEditor(cellEditorState)
        assertEquals(currentText,newState.currentText)
        val spanIndices=newState.currentTextField.annotatedString.spanStyles.map{
            it.start .. it.end
        }
        assertEquals(
            listOf(
                1 .. 2+1,
                4 .. 5+1,
                7 .. 8+1
            )
            ,
        spanIndices
        )
    }
}
