package com.qxdzbc.p6.ui.format2

import io.kotest.matchers.shouldBe
import kotlin.test.*

internal class CellFormatTableImpTest {

    @Test
    fun applyConfig() {
        val config1 = FormatConfig.random()
        val table1 = CellFormatTableImp()
        table1.getFormatConfigIncludeNullForConfig_Respectively(config1).isInvalid() shouldBe true
        val table2 = table1.applyConfig(config1)

        table2.textSizeTable shouldBe table1.textSizeTable.applyConfig(config1.textSizeConfig)
//        table2.getFormatConfigIncludeNullForConfig_Respectively(config1) shouldBe config1

    }
}
