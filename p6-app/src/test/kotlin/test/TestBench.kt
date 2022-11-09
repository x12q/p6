package test

import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.Test


class TestBench {
    @OptIn(ExperimentalContracts::class)
    fun isSt(a: Any?): Boolean {
        contract {
            returns(true) implies (a is St<*>)
        }
        return a is St<*>
    }

    fun forceOverflow(x:Int){
        forceOverflow(x)
    }


    @Test
    fun t() {
    }
}
