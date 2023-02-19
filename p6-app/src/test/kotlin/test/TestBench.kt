package test

import androidx.compose.ui.graphics.Color
import com.qxdzbc.common.test_util.TestSplitter
import kotlin.test.Test


class TestBench :TestSplitter(){
    data class A(val x:Int)
    @Test
    fun t() {
        val a1 = A(10)
        val a2 = a1.copy(10)
        println(a1 === a2)
    }
}



