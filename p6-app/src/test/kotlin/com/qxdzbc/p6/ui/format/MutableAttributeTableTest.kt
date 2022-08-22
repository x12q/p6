package com.qxdzbc.p6.ui.format

import com.qxdzbc.p6.ui.format.pack.AttributePack
import kotlin.test.*

class MutableAttributeTableTest {

    lateinit var table: MutableAttributeTable

    @BeforeTest
    fun beforeTest() {
        table = MutableAttributeTable()
    }

    @Test
    fun add() {
        val attr = MockedAttr(123)
        assertNull(table.getAttrPack(1, 1))
        table.add(1, 1, attr)
        val pack: AttributePack? = table.getAttrPack(1, 1)
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
        table.add(1, 1, attr)
        table.add(1, 2, MockedAttr(123))
        assertEquals(1, table.markedAttributes.size)
        assertSame(attr, table.markedAttributes.toList()[0].attr)
    }

    @Test
    fun add_valid() {
        val attr1 = MockedAttr(123)
        val attr2 = MockedAttr(123)
        assertNull(table.getAttrPack(1, 1))
        table.add(1, 1, attr1)
        table.add(1, 2, attr1)
        table.removeAttrFromOneCell(1, 1, attr1)
        // x: attr2 is in arg, but it does not get added because attr1 is there, and still valid
        table.add(1, 1, attr2)

        val pack11 = table.getAttrPack(1, 1)
        assertNotNull(pack11)
        assertSame(attr1, pack11.allAttrs.toList()[0])
        assertNotSame(attr2, pack11.allAttrs.toList()[0])
    }

    @Test
    fun add_invalid() {
        val attr1 = MockedAttr(123)
        val attr2 = MockedAttr(123)
        assertNull(table.getAttrPack(1, 1))
        table.add(1, 1, attr1)
        table.add(1, 2, attr1)
        table.removeAttrFromAllCell(attr1)
        // x: at this point attr1 is still in the internal table, it is only marked as invalid.
        assertTrue(table.markedAttributes.isEmpty())
        table.add(1, 1, attr2)

        val pack11 = table.getAttrPack(1, 1)
        assertNotNull(pack11)
        assertSame(attr2, pack11.allAttrs.toList()[0])
        assertNotSame(attr1, pack11.allAttrs.toList()[0])
    }

    @Test
    fun removeAttrFromAllCell() {
        val t2 = table.add(1, 3, MockedAttr(123))
            .add(1, 4, MockedAttr(123))
            .add(1, 5, MockedAttr(123))
            .removeAttrFromAllCell(MockedAttr(123))
        assertTrue(t2.markedAttributes.isEmpty())
        assertNull(t2.getAttrPack(1, 3))
        assertNull(t2.getAttrPack(1, 4))
        assertNull(t2.getAttrPack(1, 5))
    }

    @Test
    fun removeAttrFromOneCell() {
        val attr1 = MockedAttr(123)
        val attr2 = MockedAttr(123)
        val attr3 = MockedAttr(456)

        val t1 =
            table.add(1, 3, attr1)
                .add(1, 3, attr3)
                .add(1, 4, attr1)
                .add(1, 5, attr2)
                .removeAttrFromOneCell(1, 3, MockedAttr(123))
                .removeAttrFromOneCell(1, 4, MockedAttr(123))

        assertNull(t1.getAttrPack(1, 4))
        assertSame(attr3, t1.getAttrPack(1, 3)?.allAttrs?.toList()?.get(0))
        assertSame(attr1, t1.getAttrPack(1, 5)?.allAttrs?.toList()?.get(0))
        val l = t1.markedAttributes.map { it.attr }
        assertSame(attr1, l[0])
        assertSame(attr3, l[1])
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
                .removeAllAttrAt(1, 3)
        assertNull(t1.getAttrPack(1, 3))
        assertNotNull(t1.getAttrPack(1, 4))
        assertNotNull(t1.getAttrPack(1, 5))
    }

    @Test
    fun getAttr() {
    }

    @Test
    fun getAttrPack() {
    }
}
