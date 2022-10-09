package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

import androidx.compose.material.TextField
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import test.BaseTest
import kotlin.test.*

internal class CycleFormulaLockStateImpTest:BaseTest(){
    lateinit var act:CycleFormulaLockStateImp

    @BeforeTest
    override fun b() {
        super.b()
        act = ts.p6Comp.cycleFormulaLockStateAct() as CycleFormulaLockStateImp
    }

    @Test
    fun cycleFormulaLockState(){
        val m = mapOf(
            ("=F1(A1+A2)" to 0) to null,
            ("=F1(A1+A2)" to 0+1) to null,
            ("=F1(A1+A2)" to 1+1) to null,
            ("=F1(A1+A2)" to 2+1) to null,
            ("=F1(A1+A2)" to 3+1) to "=F1(\$A\$1+A2)",
            ("=F1(A1+A2)" to 4+1) to "=F1(\$A\$1+A2)",
            ("=F1(A1+A2)" to 5+1) to "=F1(\$A\$1+A2)",
            ("=F1(A1+A2)" to 6+1) to "=F1(A1+\$A\$2)",
            ("=F1(A1+A2)" to 7+1) to "=F1(A1+\$A\$2)",
            ("=F1(A1+A2)" to 8+1) to "=F1(A1+\$A\$2)",
            ("=F1(A1+A2)" to 9+1) to null,
        )
        for((input,exp) in m){
            val (formula,cursorPos) = input
            assertEquals(exp,act.cycleFormulaLockState(formula,cursorPos),input.toString())
        }
    }
}
