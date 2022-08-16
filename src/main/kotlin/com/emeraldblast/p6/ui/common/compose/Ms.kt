package com.emeraldblast.p6.ui.common.compose

import androidx.compose.runtime.*
import com.github.michaelbull.result.Ok

typealias Ms<T> = MutableState<T>
typealias St<T> = State<T>
/**
 * a convenient function for remember + mutableStateOf
 */
@Composable
fun <T> rms(o: T): Ms<T> {
    return remember { mutableStateOf(o) }
}

/**
 * a convenient function for mutableStateOf
 */
fun <T> ms(o: T): Ms<T> {
    return mutableStateOf(o)
}

/**
 * a convenient function for mutableStateOf
 */
inline fun <T> ms(f: () -> T): Ms<T> {
    return mutableStateOf(f())
}

/**
 * a convenient function for remember + mutableStateOf
 */
@Composable
fun <T> rms(f: () -> T): Ms<T> {
    return remember { mutableStateOf(f()) }
}

object MsUtils {
    fun <T : Any?> T.toMs(): Ms<T> {
        return ms(this)
    }
}
