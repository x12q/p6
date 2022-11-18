package com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state

import test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CycleFormulaLockStateImpTest : BaseTest() {
    lateinit var act: CycleFormulaLockStateImp

    @BeforeTest
    override fun b() {
        super.b()
        act = ts.comp.cycleFormulaLockStateAct() as CycleFormulaLockStateImp
    }

    @Test
    fun cycleFormulaLockState() {
        val m: Map<Pair<String, Int>, String?> =
//            mapOf(
//            ("=F1(A1+A2)" to 0) to null,
//            ("=F1(A1+A2)" to 0 + 1) to null,
//            ("=F1(A1+A2)" to 1 + 1) to null,
//            ("=F1(A1+A2)" to 2 + 1) to null,
//            ("=F1(A1+A2)" to 9 + 1) to null,
//        )+
                buildMap {
//            for (x in 3..8) {
//                put(("=F1(A1+A2)" to 3 + 1), "=F1(\$A\$1+A2)")
//            }

            for (x in 3..14) {
                put(("=F1(A1@'Sheet2')" to x + 1), "=F1(\$A\$1@'Sheet2')")
            }

//            for (x in 3..22) {
//                put(("=F1(A1@'Sheet2'@'Book1')" to x + 1), "=F1(\$A\$1@'Sheet2'@'Book1')")
//            }

        }
        for ((input, exp) in m) {
            val (formula, cursorPos) = input
            assertEquals(exp, act.cycleFormulaLockState(formula, cursorPos), input.toString())
        }
    }
}
