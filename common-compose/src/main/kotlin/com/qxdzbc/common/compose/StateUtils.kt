package com.qxdzbc.common.compose

import androidx.compose.runtime.*
import com.qxdzbc.common.compose.TFMutableState.Companion.tf
import com.qxdzbc.common.compose.TFMutableState.Companion.tfMutableStateOf


object StateUtils {
    /**
     * a convenient function for mutableStateOf
     */
    inline fun <T> ms(f: () -> T): Ms<T> {
        return tfMutableStateOf(f())
    }
    /**
     * a convenient function for mutableStateOf
     */
    fun <T> ms(o: T): Ms<T> {
        return tfMutableStateOf(o)
    }

    fun <T : Any?> T.toMs(): Ms<T> {
        return ms(this)
    }

    fun <T : Any?> T.toSt(): St<T> {
        return ms(this)
    }

    /**
     * a convenient function for remember + mutableStateOf
     */
    @Composable
    fun <T> rms(o: T): Ms<T> {
        return remember { ms(o) }
    }

    /**
     * a convenient function for remember + mutableStateOf
     */
    @Composable
    fun <T> rms(f: () -> T): Ms<T> {
        return remember { ms(f()) }
    }
}
