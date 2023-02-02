package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.test_util.TestSplitter
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.cell.state.format.text.CellFormatImp
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.*

internal class CellFormatTableImpTest :TestSplitter(){

    @Test
    fun applyConfig() {
        val config1 = FormatConfig.random()
        val table1 = CellFormatTableImp()
        preCondition {
            table1.getFormatConfigForConfig_Respectively(config1).isInvalid() shouldNotBe config1
        }
        val table2 = table1.applyConfig(config1)
        postCondition {
            table2.getFormatConfigForConfig_Respectively(config1) shouldBe config1
        }

        test("Test applying a new config with the same range address as the existing config"){
            val config2 = FormatConfig.randomize(config1)
            val table3 = table2.removeFormatByConfig_Respectively(config2).applyConfig(config2)
            postCondition {
                val c2 = table3.getFormatConfigForConfig_Respectively(config2)
                c2 shouldBe config2
            }
        }
    }

    @Test
    fun setFormat(){
        val ca = CellAddress("Q2")
        val table1 = CellFormatTableImp()
        table1.getFormat(ca).isEmpty().shouldBeTrue()

        val f1= CellFormatImp.random()
        val table2 = table1.setFormat(ca,f1)
        table2.getFormat(ca) shouldBe f1

        val f2= CellFormatImp.random()
        val table3 = table2.setFormat(ca,f2)
        table3.getFormat(ca) shouldBe f2
    }
}
