package test

import com.qxdzbc.common.compose.St
import org.testng.annotations.BeforeTest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.test.Test


class TestBench {
    @OptIn(ExperimentalContracts::class)
    fun isSt(a: Any?): Boolean {
        contract {
            returns(true) implies (a is St<*>)
        }
        return a is St<*>
    }

    fun forceOverflow(x: Int) {
        forceOverflow(x)
    }

    lateinit var x: String
    @BeforeTest
    fun b(){
        x="q"
    }
    @Test
    fun t() {

        x= "a"
        x="b"
        println(x)
    }
}

