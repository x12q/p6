package test

import com.qxdzbc.common.compose.St
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.FilenameUtils
import org.testng.annotations.BeforeTest
import java.util.concurrent.LinkedBlockingDeque
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.test.Test


class TestBench {

    @Test
    fun t() {
        println(FilenameUtils.getExtension("abc.csv"))
    }
}

