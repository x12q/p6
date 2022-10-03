package com.qxdzbc.p6.translator.autocomplete

import test.BaseTest
import kotlin.test.*

internal class FormulaAutoCompleterImpTest : BaseTest(){
    lateinit var c:FormulaAutoCompleter
    @BeforeTest
    override fun b() {
        super.b()
        c = this.ts.p6Comp.formulaCompleter()
    }

    @Test
    fun completeFormula(){
        assertEquals("=SUM(A1)", c.completeParenthesis("=SUM(A1"))
        assertEquals("=SUM(A1,SUM(A2))", c.completeParenthesis("=SUM(A1,SUM(A2"))
        assertEquals("=SUM(A1,SUM(F2(A2)))", c.completeParenthesis("=SUM(A1,SUM(F2(A2"))
        assertEquals("=A1", c.completeParenthesis("=A1"))
        assertEquals("=(A1+2)", c.completeParenthesis("=(A1+2"))
    }

}
