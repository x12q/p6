package com.qxdzbc.p6.ui.common.color_generator

import kotlin.test.*

internal class FormulaColorMapImpTest{
    @Test
    fun getColor(){
        val cp = FormulaColorGeneratorImp(RandomColorGenerator())
        assertEquals(2,cp.getColors(2).size)
        assertEquals(1,cp.getColors(1).size)
        assertEquals(44,cp.getColors(44).size)
    }
}
