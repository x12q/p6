package com.qxdzbc.p6.ui.document.cell.state.format.text

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import kotlin.test.*

internal class CellFormatImpTest{
    @Test
    fun isEmpty(){
        val f1 = CellFormatImp()
        f1.isEmpty().shouldBeTrue()
        val f2 = CellFormatImp(textSize = 1f)
        f2.isEmpty().shouldBeFalse()
        val f3 = CellFormatImp.random()
        f3.isEmpty().shouldBeFalse()
    }
}

