package test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class TestBench {
    @OptIn(ExperimentalContracts::class)
    fun isSt(a:Any?):Boolean{
        contract{
            returns(true) implies (a is St<*>)
        }
        return a is St<*>
    }


    @Test
    fun t() {

    }
}
