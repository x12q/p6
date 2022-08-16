package com.emeraldblast.p6.ui.format.pack

import com.emeraldblast.p6.ui.format.MockedAttr
import com.emeraldblast.p6.ui.format.marked.MarkedAttributes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ImmutableAttributePackTest {

    @Test
    fun constructor() {
        val count = 10

        val mset = (0 until count).map { MockedAttr(it) }.map {
            MarkedAttributes.valid(it)
        }.toMutableSet()

        val pack = ImmutableAttributePack(mset)

        assertEquals(mset.size, pack.allAttrs.size)
        assertEquals(mset.size, pack.allMarkedAttrs.size)
        assertEquals(mset.size, pack.size)
    }

    @Test
    fun remove() {
        val count = 10
        val mset = (0 until count).map { MockedAttr(it) }.map {
            MarkedAttributes.valid(it)
        }.toMutableSet()
        val pack = ImmutableAttributePack(mset)
        val p2 = pack.remove(MarkedAttributes.valid(MockedAttr(3)))
        assertEquals(count - 1, p2.allAttrs.size)
        for (e in p2.allAttrs) {
            assertNotEquals(3, (e as MockedAttr).i)
        }
    }

    @Test
    fun add() {
        val count = 10
        val mset = (0 until count).map { MockedAttr(it) }.map {
            MarkedAttributes.valid(it)
        }.toMutableSet()
        val pack = ImmutableAttributePack(mset)
        val p2 = pack.add(MarkedAttributes.valid(MockedAttr(99)))
        var c= 0
        for(e in p2.allAttrs){
            if((e as MockedAttr).i == 99){
                c+=1
            }
        }
        assertEquals(1,c)
    }

    @Test
    fun cleanInvalidAttribute() {
        val count = 10
        val mset = (0 until count).map { MockedAttr(it) }.map {
            MarkedAttributes.valid(it)
        }.toMutableSet()
        val invalidSet = (30 until 35).map { MockedAttr(it)}.map {
            MarkedAttributes.invalid(it)
        }.toMutableSet()
        val pack = ImmutableAttributePack((mset+invalidSet).toMutableSet())
        val p2 = pack.removeInvalidAttribute()
        assertEquals(mset.size,p2.size)
        assertEquals(mset,p2.allMarkedAttrs)
    }
}
