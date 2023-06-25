package com.qxdzbc.p6.app.action.cell_editor.color_formula

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.ui.app.cell_editor.actions.CellEditorAction
import io.kotest.matchers.shouldBe
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
    fun makeAnnotatedString(){

    }
    @Test
    fun `colorFormula preserve text content on err text`(){
        test("test that color formula action preserve the text content when the text is an erroneous formula"){
            val wbwsSt = ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!
            cellEditorAct.openCellEditor(wbwsSt)
            val text = "=B1+SUM(D)"
            cellEditorAct.changeRawText(text)
            ts.sc.cellEditorState.currentText shouldBe text
            val newState=act.colorCurrentTextInCellEditor(cellEditorState)
            postCondition {
                newState.currentText shouldBe text
            }
        }
    }


    @Test
    fun `colorFormula preserve text content on ok text`(){
        val wbwsSt = ts.sc.getWbWsSt(ts.wbKey1,ts.wsn1)!!
        cellEditorAct.openCellEditor(wbwsSt)
        cellEditorAct.changeRawText("=F1")

        assertEquals("=F1",ts.sc.cellEditorState.currentText)

        val t3 = "=A1+B2+C2"
        cellEditorAct.changeRawText(t3)
        val currentText = cellEditorState.currentTextField.text
        val newState=act.colorCurrentTextInCellEditor(cellEditorState)
        assertEquals(currentText,newState.currentText)
        val spanIndices=newState.currentTextField.annotatedString.spanStyles.map{
            it.start .. it.end
        }
        spanIndices shouldBe listOf(
            1 .. 2+1,
            4 .. 5+1,
            7 .. 8+1
        )
    }
}
