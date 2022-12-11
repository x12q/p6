package com.qxdzbc.common.flyweight

import com.qxdzbc.common.flyweight.Flyweight
import com.qxdzbc.common.flyweight.FlyweightImp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class FlyweightImpTest {
    val number = 12f
    @Test
    fun all() {
        var marked: Flyweight<Float> = FlyweightImp(number,1)

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
