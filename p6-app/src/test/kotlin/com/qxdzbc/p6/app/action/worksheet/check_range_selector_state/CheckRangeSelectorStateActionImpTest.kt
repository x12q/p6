package com.qxdzbc.p6.app.action.worksheet.check_range_selector_state

import org.junit.Assert
import kotlin.test.*

internal class CheckRangeSelectorStateActionImpTest {
    val act = CheckRangeSelectorStateActionImp()

    @Test
    fun checkMidWay(){
        listOf(
            "=F1()" to 4,
        ).forEach {(str,pos)->
            assertTrue(act.check(str,pos))
        }
        listOf(
            "F1()" to 3,
            "=F1()" to 5,
        ).forEach {(str,pos)->
            assertFalse(act.check(str,pos))
        }
    }

    @Test
    fun checkAtLength() {
        val m = listOf(
            "=",
            "=S(",
            "=1+2+",
            "=1+2/",
            "=1+2*",
            "=1+2-",
            "=abc+",
            "=S(A1+",
            "=S(A1/",
            "=S(A1*",
            "=S(A1-",
            "=S(A1-(",
            "=S(A1:",
        )

        for (str in m) {
            Assert.assertTrue(act.checkAtLength(str))
        }

        val m2 = listOf(
            "",
            "A1+",
            "A1-",
            "A1*",
            "A1/",
            "1+2+",
            "1+2-",
            "1+2/",
            "1+2*",
            "A1:",
            "abc",
        )
        for (str in m2) {
            Assert.assertFalse(act.checkAtLength(str))
        }
    }


}
