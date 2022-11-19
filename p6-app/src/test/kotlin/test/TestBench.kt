package test

import com.qxdzbc.common.compose.St
import org.testng.annotations.BeforeTest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.test.Test


class TestBench {
    @Test
    fun t() {
        var x= "a"
        x="b"
        println(x)
    }
}

