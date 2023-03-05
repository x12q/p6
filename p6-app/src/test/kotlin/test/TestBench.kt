package test

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.test_util.TestSplitter
import kotlin.test.Test


class TestBench :TestSplitter(){
    @JvmInline
    value class MyAny(val i:Any?)
    data class Q(val i:Int, val s:String)
    @JvmInline
    value class Vl2(val i: Q)

    @Test
    fun t() {
        val ma = MyAny(123)
        val ma2 = MyAny(ma)
        val q = Q(123,"abc")
        val ma3 = MyAny(q)
        println(ma)
        println(ma2)
        println(ma3)
    }
}



