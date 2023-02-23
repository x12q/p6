package test.example

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.common.compose.StateUtils.rms

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)


fun main() {
    singleWindowApplication(title = "Context menu") {
        var v by rms("abc")
            Box(modifier=Modifier.border(1.dp, Color.Black)){
                BasicTextField(v, onValueChange = {
                    v = it
                },
                )
            }
    }
}
