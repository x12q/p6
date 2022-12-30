package com.qxdzbc.p6.ui.format2

import com.qxdzbc.common.test_util.TestSplitter
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
            table2.textSizeTable shouldBe table1.textSizeTable.applyConfig(config1.textSizeConfig)
            table2.getFormatConfigForConfig_Respectively(config1) shouldBe config1
        }
    }
}
