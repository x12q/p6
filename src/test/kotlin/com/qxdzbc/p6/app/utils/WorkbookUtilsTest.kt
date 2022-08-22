package com.qxdzbc.p6.app.utils

import com.qxdzbc.p6.app.common.utils.WorkbookUtils
import org.junit.Test
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.assertEquals


internal class WorkbookUtilsTest{
    @Test
    fun generateNewSheetName(){
        val n1 = WorkbookUtils.generateNewSheetName(listOf("abc","qwe","sheet","Sheet"))
        assertEquals("Sheet1",n1)

        val n2 = WorkbookUtils.generateNewSheetName(listOf("Sheet1","sheet2"))
        assertEquals("Sheet2",n2)

        val n3 = WorkbookUtils.generateNewSheetName(listOf("Sheet100"))
        assertEquals("Sheet101",n3)

        val random = Random(100)
        for (x in 1 .. 1000){
            val randomInt = abs(random.nextInt())
            val n = WorkbookUtils.generateNewSheetName(listOf("Sheet$randomInt"))
            assertEquals("Sheet${randomInt+1}",n)
        }

        val n4 = WorkbookUtils.generateNewSheetName(listOf("Sheet3","Sheet200"))
        assertEquals("Sheet201",n4)
    }
}
