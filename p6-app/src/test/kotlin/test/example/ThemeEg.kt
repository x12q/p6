package test.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.common.view.BorderBox

fun main(){
    P6TestApp{
        Row{
            MaterialTheme {
                BorderBox {
                    Column {
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.primary).size(20.dp)){}
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.onError).size(20.dp)){}
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.secondary).size(20.dp)){}
                    }
                }
            }

            MaterialTheme{
                BorderBox {
                    Column {
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.primary).size(20.dp)){}
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.onError).size(20.dp)){}
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.secondary).size(20.dp)){}
                    }
                }
            }
        }

    }
}
