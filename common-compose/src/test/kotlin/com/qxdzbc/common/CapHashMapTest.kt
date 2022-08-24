package com.qxdzbc.common

import com.qxdzbc.common.CapHashMap
import kotlin.test.*

internal class CapHashMapTest{
    lateinit var map: CapHashMap<Int, String>
    @BeforeTest
    fun b(){
        map = CapHashMap(3)
    }

    @Test
    fun t1(){
        for(x in 1 .. 100){
            map.put(x,"$x")
        }
        assertEquals(3,map.size)
        val e = listOf(98 to "98", 99 to "99", 100 to "100")
        val e2 = map.entries.toList().map { it.key to it.value }
        assertEquals(e,e2)
    }
}
