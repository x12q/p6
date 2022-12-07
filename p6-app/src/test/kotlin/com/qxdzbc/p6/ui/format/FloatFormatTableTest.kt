package com.qxdzbc.p6.ui.format

import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.format.marked.MarkedAttributes
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.test.*

class FloatFormatTableTest {

    lateinit var table: FloatFormatTable

    @BeforeTest
    fun beforeTest() {
        table = FloatFormatTable()
    }

    @Test
    fun addSizeAttr() {
        val attr = MarkedAttributes.valid(MockedAttr(123)).upCounter()
        val msAttr=ms(attr)
        table.getFloatMarkedAttr(10f).shouldBeNull()
        val t2 = table.addFloatMarkedAttr(10f,msAttr)
        t2.getFloatMarkedAttr(10f) shouldBe msAttr
    }

    /**
     * add duplicate attribute
     */
    @Test
    fun add_duplicate() {
        val attr = MockedAttr(123)

    }
}
