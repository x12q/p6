package test

import com.qxdzbc.p6.ui.format2.RangeAddressSetImp
import kotlin.test.Test


class TestBench {

    @Test
    fun t() {
        val r = (1 .. 100).step(10)
        r.forEach {
            println(it)
        }
    }
}



