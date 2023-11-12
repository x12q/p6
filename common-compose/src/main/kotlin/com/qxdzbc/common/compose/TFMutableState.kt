package com.qxdzbc.common.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot


/**
 * TFMutableState = Thread-safe mutable state.
 * Setting value to this state object is thread-safe.
 */
class TFMutableState<T>(
    private val mutableState: MutableState<T>
): MutableState<T> {
    override var value: T
        get() {
            return mutableState.value
        }
        set(value) {
            Snapshot.withMutableSnapshot {
                mutableState.value = value
            }
        }

    override fun component1(): T {
        return value
    }

    override fun component2(): (T) -> Unit = { value = it }

    companion object{
        /**
         * Construct a [TFMutableState] which is a [MutableState]
         */
        fun <T> MutableState<T>.tf(): TFMutableState<T> {
            return TFMutableState(this)
        }
        /**
         * Construct a [TFMutableState]
         */
        fun <T> tfMutableStateOf(i:T):TFMutableState<T>{
            return TFMutableState(mutableStateOf(i))
        }
    }
}
