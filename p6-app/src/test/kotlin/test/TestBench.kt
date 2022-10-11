package test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import kotlin.test.Test


class TestBench {

    class NumQ(var i: Int) {
        operator fun getValue(qq: Any?, property: KProperty<*>): Int {
            return i
        }

        operator fun setValue(qq: Any?, property: KProperty<*>, i: Int) {
            this.i = i
        }
    }

    class QQ {
        val numQ = NumQ(123)
        var i2: Int by numQ
    }

    data class A(val i: Int, val s: String)

    var s: String by Delegates.notNull<String>()

    fun aaaa() = sequence<Int> {
        var param = Pair(0, 1)
        while (true) {
            yield(param.first)
            param = Pair(param.second, param.first + param.second)
        }
    }

    @Test
    fun t() {
        val t = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Red)){
                append("qwe")
            }
            withStyle(style = SpanStyle(color = Color.Green)){
                append("123")
            }
        }

        println(s)
    }
}
