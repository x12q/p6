package test.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.view.MBox

/**
 * See console for detail
 */
fun main() {
    singleWindowApplication {
        val s1: Ms<State1> = remember { mutableStateOf(State1(100)) }
        val s2: Ms<State2> = remember { mutableStateOf(State2("state 2 ")) }
        val counter = remember { mutableStateOf(1) }
        Row {
            Column {
                Button(onClick = {
                    s1.value = s1.value.copy(v = s1.value.v + 1)
                }) {
                    Box {
                        println("change state 1")
                        Text("Good - change state 1: ${s1.value.v}")
                    }
                }

                Button(onClick = {
                    s2.value = s2.value.copy(str = s2.value.str + "z")
                }) {
                    Box {
                        // x: this only changes state 2, but it will redraw all the views
                        println("change state 2")
                        Text("Bad - change state 2: ${s2.value.str}")
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
                    Text("Counter: ${counter.value}")
                }
                MBox {
                    // this use MBox, changing state 1 will only re-draw this view, other views are kept as they were
                    Text("${s1.value}")
                }
                Box {
                    // this does not use MBox => when s2 is changed, it will re-draw all the view
                    Text("${s2.value}")
                }
            }
        }
    }
}


data class CentralState(val state1: MutableState<State1>, val state2: MutableState<State2>) {

    override fun toString(): String {
        return "${state1.value}\n${state2.value}"
    }
}

data class State1(val v: Int)
data class State2(val str: String)
