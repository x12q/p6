package com.emeraldblast.p6.ui.example

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.singleWindowApplication
import com.emeraldblast.p6.ui.common.view.MBox

/**
 * See console for detail
 */
@OptIn(ExperimentalFoundationApi::class)
fun main() {
    singleWindowApplication {
        val cen = CentralState.remember(
            mutableStateOf(State1(100)),
            mutableStateOf(State2("state 2 "))
        )
        val s = cen.value.state1
        val s2 = cen.value.state2
        val counter = remember { mutableStateOf(1) }
        Row {
            Column {
                Button(onClick = {
                    s.value = s.value.copy(v = s.value.v + 1)
                }) {
                    Box {
                        println("invoke state 1")
                        Text("Good change: ${s.value.v}")
                    }
                }

                Button(onClick = {
                    s2.value = s2.value.copy(str = s2.value.str + "z")
                }) {
                    Box {
                        println("invoke state 2")
                        Text("Bad change: ${s2.value.str}")
                    }
                }

                Button(onClick = {
                    counter.value = counter.value + 1
                }) {
                    Column {
                        Box {
                            println("invoke 3")
                            Text("Counter: ${counter.value}")
                        }
                    }
                }
                MBox {
                    Text("C3: ${counter.value}")
                }
                MBox {
                    // this use MBox, changing s will only recompose what is needed
                    Text("${s.value}")
                }
                Box {
                    // this does not use MBox => when s2 is changed, it will re-compose the whole column
                    Text("${s2.value}")
                }
            }
        }
    }
}


data class CentralState(val state1: MutableState<State1>, val state2: MutableState<State2>) {
    companion object {
        @Composable
        fun remember(state1: MutableState<State1>, state2: MutableState<State2>): MutableState<CentralState> {
            val s1 = remember { state1 }
            val s2 = remember { state2 }
            return remember { mutableStateOf(CentralState(s1, s2)) }
        }

        @Composable
        fun rememberNot(state1: MutableState<State1>, state2: MutableState<State2>): MutableState<CentralState> {
            val s1 = remember { state1 }
            val s2 = remember { state2 }
            return mutableStateOf(CentralState(s1, s2))
        }
    }

    override fun toString(): String {
        return "${state1.value}\n${state2.value}"
    }
}

data class State1(val v: Int)
data class State2(val str: String)
