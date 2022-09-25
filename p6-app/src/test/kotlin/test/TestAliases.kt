package test

import kotlin.test.*

fun <T>eq(e:T, a:T, msg:String? = null){
    assertEquals(e,a,msg)
}
