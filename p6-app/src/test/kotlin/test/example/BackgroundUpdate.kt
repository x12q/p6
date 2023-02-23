package test.example

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.qxdzbc.common.compose.Ms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    application{
        val appScope = this
        val cs = rememberCoroutineScope()
        val xStateMs = remember { mutableStateOf(XState(mutableStateOf(QState(1)))) }
        val qStateMs = xStateMs.value.qStateMs
        val onClick:()->Unit = {
            cs.launch(Dispatchers.Default) {
                while(true){
                    qStateMs.value = qStateMs.value.up()
                    delay(1000)
                }
            }
        }
        qWindow(appScope,xStateMs.value,onClick)
    }
}

@Composable
fun qWindow(appScope:ApplicationScope, xState: XState, onClick:()->Unit ){
    val size = DpSize(500.dp, 500.dp)
    val wState = rememberWindowState(size=size)
    // this line cause the push-to-top behavior
//    val qState = xState.qState
    Window(
        onCloseRequest = appScope::exitApplication,
        title = "Test app",
        state = wState
    ) {
        Column {
            Text(text = "number ${xState.i10}",)
            Text(text = "number ${xState.i}",)
            Button(onClick){
                Text("Start")
            }
        }
    }
}

data class QState(val i:Int){
    val i10: Int get()=i*10

    fun up(): QState {
        return this.copy(i=i+1)
    }
}

data class XState(val qStateMs:Ms<QState>){
    val i get() = qState.i // work too
    var qState by qStateMs
    val i10:Int get(){
        return qStateMs.value.i10
    }
}
