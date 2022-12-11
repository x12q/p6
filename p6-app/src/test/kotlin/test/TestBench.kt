package test

import com.qxdzbc.common.compose.St
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FilenameUtils
import org.testng.annotations.BeforeTest
import java.util.concurrent.LinkedBlockingDeque
import javax.swing.text.html.HTML
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertTrue


class TestBench {
    data class Q(val b:Boolean)
    @Test
    fun t() {
        val b:Boolean = true
        val b2 = b
        val q = Q(b)
        println(b.hashCode())
        println(q.b.hashCode())
        assertTrue(b === q.b)

    }
}

