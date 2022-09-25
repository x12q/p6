package test
import com.qxdzbc.p6.app.communication.res_req_template.WithErrorReport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.properties.Delegates
import kotlin.test.*
class TestBench {

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
        val k = 0.0
        println(k!=0.toDouble())
    }
}
