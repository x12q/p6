package test

import com.qxdzbc.common.compose.St
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FilenameUtils
import org.testng.annotations.BeforeTest
import test.splitter.TestContext
import java.util.concurrent.LinkedBlockingDeque
import javax.swing.text.html.HTML
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.test.Test


class TestBench {
    fun html(init: TestContext.() -> Unit) {

    }
    @Test
    fun t() {
        html {


        }
    }
}

