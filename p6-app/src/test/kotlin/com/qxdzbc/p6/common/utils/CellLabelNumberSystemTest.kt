package com.qxdzbc.p6.common.utils

import com.qxdzbc.p6.common.utils.CellLabelNumberSystem
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CellLabelNumberSystemTest {
    val input = mapOf(
        "A" to 1 ,
        "B" to 2,
        "C" to 3,
        "D" to 4,
        "Z" to 26,
        "AA" to 27,
        "AZ" to 52,
        "BA" to 53,
        "AAA" to 703,
        "AW" to 49,
        "SJ" to 504,
        "AII" to 919,
        "AEO" to 821,
        "ABO" to 743,
        "AHZ" to 910,
        "YZ" to 676,
        "FXSHRXW" to Int.MAX_VALUE
    ).map { (k,v)->Pair(k,v) }.toMap()

    @Test
    fun toDecimal() {
        for ((k, v) in input) {
            assertEquals(v, CellLabelNumberSystem.labelToNumber(k), k)
        }
    }

    @Test
    fun fromDecimal() {
        for ((k, v) in input) {
            assertEquals(k, CellLabelNumberSystem.numberToLabel(v),v.toString())
        }
        println(CellLabelNumberSystem.numberToLabel(Int.MAX_VALUE))
    }

    @Test
    fun comprehensive() {
        val exceptionAt = mutableMapOf<Int,Exception>()
        val map = mutableMapOf<String,Int>()
        for (x in 1 .. 1_000) {
            try{

                map[CellLabelNumberSystem.numberToLabel(x)] = x
            }catch (e:Exception){
                exceptionAt.put(x,e)
            }
        }
        if(exceptionAt.isNotEmpty()){
            exceptionAt.forEach {
                println(it.key)
            }
        }
        assertTrue(exceptionAt.isEmpty())

        for((k,v) in map){
            assertEquals(v, CellLabelNumberSystem.labelToNumber(k))
        }

    }
}
