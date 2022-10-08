package test
import com.github.michaelbull.result.Ok
import com.qxdzbc.p6.translator.partial_extrator.PartialJvmFormulaVisitor
import com.qxdzbc.p6.translator.partial_extrator.PartialFormulaTreeExtractor
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertTrue


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
        val formula = "=F1(A1+A2"
        val te = PartialFormulaTreeExtractor()
        val tree = te.extractTree(formula)
        val visitor = PartialJvmFormulaVisitor()

        assertTrue(tree is Ok)
        val out = visitor.visit(tree.value)
        println(out)
    }
}
