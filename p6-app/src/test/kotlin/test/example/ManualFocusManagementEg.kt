package test.example

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.qxdzbc.common.compose.StateUtils.ms

data class FS2(
    val f1: Boolean = true,
    val f2: Boolean = false,
    val fr1:FocusRequester = FocusRequester(),
    val fr2:FocusRequester = FocusRequester(),
) {
    fun toF1(): FS2 = this.copy(f1 = true, f2 = false)
    fun toF2(): FS2 = this.copy(f1 = false, f2 = true)
}

fun main() = application {
    Window(
        state = WindowState(size = WindowSize(350.dp, 450.dp)),
        onCloseRequest = ::exitApplication
    ) {
        val fsMs = remember { ms(FS2()) }
        var fs by fsMs
        val fr1 = fs.fr1
        val fr2 = fs.fr2
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                OutlinedTextField("${fs.f1}",
                    singleLine = true,
                    onValueChange = {},
                    modifier = Modifier
                        .focusRequester(fr1)

                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField("${fs.f2}",
                    onValueChange = {},
                    modifier = Modifier
                        .focusRequester(fr2)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    fr1.requestFocus()
                }) {
                    Text("To F1")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    fr2.requestFocus()
                }) {
                    Text("To F2")
                }
            }
        }
    }
}
