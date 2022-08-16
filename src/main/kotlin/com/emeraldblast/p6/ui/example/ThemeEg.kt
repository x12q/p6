package com.emeraldblast.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.testApp
import com.emeraldblast.p6.ui.common.view.BorderBox
import com.emeraldblast.p6.ui.theme.P6DarkColors
import com.emeraldblast.p6.ui.theme.P6GrayColors

fun main(){
    testApp{
        Row{
            MaterialTheme(
                colors = P6DarkColors
            ) {
                BorderBox {
                    Column {
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.primary).size(20.dp)){}
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.onError).size(20.dp)){}
                        BorderBox(modifier = Modifier.background(MaterialTheme.colors.secondary).size(20.dp)){}
                    }
                }
            }

            MaterialTheme(
                colors = P6GrayColors
            ) {
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
