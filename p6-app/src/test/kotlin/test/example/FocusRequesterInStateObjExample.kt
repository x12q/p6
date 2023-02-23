package test.example

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms

data class FRState(
    val fr1:FocusRequester = FocusRequester(),
    val fr2:FocusRequester = FocusRequester(),
    val fc1Ms:Ms<Boolean> = ms(false),
    val fc2Ms:Ms<Boolean> = ms(false),
){

    fun setFc1(i:Boolean){
        println("fc1: $i")
        fc1Ms.value= i
    }

    fun setFc2(i:Boolean){
        println("fc2: $i")
        fc2Ms.value= i
    }

    fun focusOn1(){
        fr1.requestFocus()
    }
    fun focusOn2(){
        fr2.requestFocus()
    }
}

fun main() = application {
    Window(
        state = WindowState(size = WindowSize(350.dp, 450.dp)),
        onCloseRequest = ::exitApplication
    ) {

        val frStateMs = remember{ms(FRState())}

        val text = remember { mutableStateOf("view 1") }
        val text2 = remember { mutableStateOf("view 2") }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(50.dp)
            ) {
                Button(
                    onClick ={
                             frStateMs.value.focusOn1()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Focus on 1")
                }

                Button(
                    onClick ={
                        frStateMs.value.focusOn2()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Focus on 2")
                }

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = text.value,
                    singleLine = true,
                    onValueChange = { text.value = it },
                    modifier = Modifier
                        .focusRequester(frStateMs.value.fr1)
                        .onFocusChanged {
                            frStateMs.value.setFc1(it.isFocused)
                        }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = text2.value,
                    singleLine = true,
                    onValueChange = { text.value = it },
                    modifier = Modifier
                        .focusRequester(frStateMs.value.fr2)
                        .onFocusChanged {
                            frStateMs.value.setFc2(it.isFocused)
                        }
                )
            }
        }
    }
}
