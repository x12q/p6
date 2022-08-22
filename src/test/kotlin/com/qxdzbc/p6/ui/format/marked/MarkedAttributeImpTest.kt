package com.qxdzbc.p6.ui.format.marked

import androidx.compose.ui.Modifier
import com.qxdzbc.p6.ui.format.FormatAttribute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class MarkedAttributeImpTest {
    object MockAttr : FormatAttribute{
        override val modifier: Modifier = Modifier
    }
    @Test
    fun all() {
        var marked:MarkedAttribute = MutableMarkedAttribute(MockAttr,true,1)
        assertTrue(marked.isValid)
        assertFalse(marked.isNotValid)
        marked = marked.switch()
        assertTrue(marked.isNotValid)
        assertFalse(marked.isValid)

        assertEquals(1,marked.refCounter)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.upCounter()
        assertEquals(2,marked.refCounter)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.downCounter()
        assertEquals(1,marked.refCounter)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.changeCounterBy(100)
        assertEquals(101,marked.refCounter)
        assertTrue(marked.isCounterNotZero)
        assertFalse(marked.isCounterZero)

        marked = marked.changeCounterBy(-1000)
        assertEquals(0,marked.refCounter)
        assertFalse(marked.isCounterNotZero)
        assertTrue(marked.isCounterZero)
    }
}
