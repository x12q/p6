package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.ui.format.pack.AttributePack
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlin.test.*

class ImmutableCellFormatTableTest {

    lateinit var table: ImmutableCellFormatTable

    @BeforeTest
    fun beforeTest() {
        table = ImmutableCellFormatTable()
    }

    @Test
    fun add() {
        val attr = MockedAttr(123)
        assertNull(table.getAttrPack(1, 1))
        val t2 = table.add(1, 1, attr)
        val pack: AttributePack? = t2.getAttrPack(1, 1)
        assertNotNull(pack)
        assertEquals(1, pack.size)
        assertEquals(attr, pack.allAttrs.toList()[0])
    }

    /**
     * add duplicate attribute
     */
    @Test
    fun add_duplicate() {
        val attr = MockedAttr(123)
        assertNull(table.getAttrPack(1, 1))
        val t2 = table.add(1, 1, attr)
            .add(1, 2, MockedAttr(123))
        assertEquals(1, t2.markedAttributes.size)
        assertSame(attr, t2.markedAttributes.toList()[0].attr)
    }

    @Test
    fun `try to overwrite valid attr `() {
        val attr1 = MockedAttr(123)
        val attr2 = MockedAttr(123)
        assertNull(table.getAttrPack(1, 1))
        val t2 = table
            .add(1, 1, attr1)
            .add(1, 2, attr1)
            .removeOneAttrFromOneCell(1, 1, attr1)
            .add(1, 1, attr2)

        val pack11 = t2.getAttrPack(1, 1)
        pack11.shouldNotBeNull()
        val l = pack11.allAttrs.toList()
        l[0] shouldBeSameInstanceAs attr1
        l[0] shouldNotBeSameInstanceAs attr2
    }

    @Test
    fun removeOneAttrFromOneCell() {
        val attr1 = MockedAttr(123)
        val attr2 = MockedAttr(123)
        val attr3 = MockedAttr(456)

        val t1 =
            table
                .add(1, 3, attr1)
                .add(1, 3, attr3)
                .add(1, 4, attr1)
                .add(1, 5, attr2)
                .removeOneAttrFromOneCell(1, 3, MockedAttr(123))
                .removeOneAttrFromOneCell(1, 4, MockedAttr(123))

        assertNull(t1.getAttrPack(1, 4))
        assertSame(attr3, t1.getAttrPack(1, 3)?.allAttrs?.toList()?.get(0))
        assertSame(attr1, t1.getAttrPack(1, 5)?.allAttrs?.toList()?.get(0))
        val l = t1.markedAttributes.map { it.attr }
        l.size shouldBe 2
        attr1 shouldBeSameInstanceAs l[0]
        attr3 shouldBeSameInstanceAs l[1]
    }

    @Test
    fun removeAllAttrAt() {
        val attr1 = MockedAttr(123)
        val attr2 = MockedAttr(123)
        val attr3 = MockedAttr(456)

        val t1 =
            table.add(1, 3, attr1)
                .add(1, 3, attr3)
                .add(1, 4, attr1)
                .add(1, 5, attr2)
                .removeAllAttrFromOneCell(1, 3)
        assertNull(t1.getAttrPack(1, 3))
        assertNotNull(t1.getAttrPack(1, 4))
        assertNotNull(t1.getAttrPack(1, 5))
        t1.markedAttributes.map { it.attr }[0] shouldBeSameInstanceAs attr1
    }

    @Test
    fun getAttr() {
    }

    @Test
    fun getAttrPack() {
    }
}
