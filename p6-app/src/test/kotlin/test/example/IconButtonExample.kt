package test.example

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.common.compose.view.MBox

// x: https://fonts.google.com/icons
fun main(){
    P6TestApp(size= DpSize(100.dp,100.dp)){
        Row{
            IconButton(onClick = {
                println("Clicked")
            }){
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Localized description"
                )
            }

            MBox{
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description"
                )
            }

            MBox{
                // x: /home/abc/Documents/gits/project2/p6/src/main/resources/icon/toggle_on_FILL0_wght400_GRAD0_opsz48.svg


                Icon(
                    Icons.Filled.ContentCopy,
                    contentDescription = "",
                    tint = Color.Red
                )
            }

        }
    }
}
