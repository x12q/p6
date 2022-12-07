package com.qxdzbc.p6.ui.format.marked

import com.qxdzbc.p6.ui.format.FormatAttribute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class MarkedAttributeImpTest {
    object MockAttr : FormatAttribute<Float>{
        override val attrValue: Float
            get() = 12f
    }
    @Test
    fun all() {
        var marked:MarkedAttribute<Float> = MutableMarkedAttribute(MockAttr,1)

        assertEquals(1,marked.refCount)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.upCounter()
        assertEquals(2,marked.refCount)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.downCounter()
        assertEquals(1,marked.refCount)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.changeCounterBy(100)
        assertEquals(101,marked.refCount)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.changeCounterBy(-1000)
        assertEquals(0,marked.refCount)
        assertFalse(marked.isCounterNotZero)
        assertTrue(marked.isCounterZero)
    }
}
