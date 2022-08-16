package com.emeraldblast.p6

import org.junit.function.ThrowingRunnable
import kotlin.test.fail
import org.junit.Assert.assertThrows as AT

@Deprecated("use assertFails instead")
inline fun <reified T : Throwable> assertThrows(message: () -> String, crossinline f: () -> Unit) {

    AT(message(),T::class.java,object:ThrowingRunnable{
        override fun run() {
            f()
        }
    })
//    try {
//        f()
//    } catch (e: Exception) {
//        assertTrue(e is T, message())
//        return
//    }
//    fail(message())
}
@Deprecated("use assertFails instead")
inline fun <reified T : Throwable> assertThrows(message: String, crossinline f: () -> Unit) {
    assertThrows<T>({ message }, f)
}
@Deprecated("use assertFails instead")
inline fun <reified T : Throwable> assertThrows(crossinline f: () -> Unit) {
    assertThrows<T>({ "should throw ${T::class.java.canonicalName}" }, f)
}
@Deprecated("use assertFails instead")
fun assertNotThrows(f: () -> Unit) {
    try {
        f()
    } catch (e: Throwable) {
        fail( "shouldn't  throw ${e::class.java.canonicalName}")
    }
}

