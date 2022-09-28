package test
import com.qxdzbc.p6.app.communication.res_req_template.WithErrorReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import kotlin.test.*
class TestBench {

    class NumQ(var i: Int){
        operator fun getValue(qq: Any?, property: KProperty<*>): Int {
            return i
        }

        operator fun setValue(qq: Any?, property: KProperty<*>, i: Int) {
             this.i = i
        }
    }
    class QQ{
        val numQ = NumQ(123)
        var i2:Int by numQ
    }

    data class A(val i:Int, val s:String)
    var s:String by Delegates.notNull<String>()
    fun aaaa () = sequence<Int> {
        var param = Pair(0,1)
        while(true){
            yield(param.first)
            param = Pair(param.second,param.first+param.second)
        }
    }
    @Test
    fun t(){
        val q = QQ()
        println(q.i2)
        q.i2 = 321
        println(q.numQ.i)
    }
}
