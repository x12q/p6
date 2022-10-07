package test
import com.qxdzbc.p6.bench.n2
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.CharCommandsVisitor
import org.apache.commons.text.diff.CommandVisitor
import org.apache.commons.text.diff.StringsComparator
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import kotlin.test.Test


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

    fun generateNumbers(start:Double,count:Int):List<Double>{
        val l = mutableListOf<Double>()
        var x = 0
        while(x<count){
            l.add(start+0)
            x++
        }
        return l
    }
    @Test
    fun t(){
        println(generateNumbers(1.2,10))
    }
}
