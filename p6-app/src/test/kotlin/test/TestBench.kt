package test

import com.google.gson.Gson
import com.qxdzbc.common.compose.St
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.Test
import kotlin.test.assertNotNull


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


    @Test
    fun t() {

    }

}
