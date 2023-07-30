package com.qxdzbc.p6.bench

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.ms
import dagger.multibindings.ClassKey
import dagger.multibindings.StringKey
import test.TestSample
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.random.Random
import kotlin.test.Test

val random = Random(123)

@OptIn(ExperimentalContracts::class)
fun Any?.n2(): Boolean {
    contract {
        returns(true) implies (this@n2 != null)
        returns(false) implies (this@n2 == null)
    }
    return this@n2 != null
}

class Bench {
    @OptIn(ExperimentalContracts::class)
    fun nn(z: Any?): Boolean {
        contract {
            returns(true) implies (z != null)
            returns(false) implies (z == null)
        }
        return z != null
    }

    interface A{
        fun doWork():Int
    }
    class AImp(val a:Int):A{
        override fun doWork(): Int {
            return a
        }
    }

    @Test
    fun z() {
        println("a line")
    }
}


