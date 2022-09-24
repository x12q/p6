package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.common.compose.view.MBox

fun main(){
    P6TestApp(size= DpSize(300.dp,300.dp)){

        BorderBox (modifier = Modifier.requiredWidthIn(30.dp,200.dp).height(30.dp)){
            MBox(modifier = Modifier.matchParentSize()){
                Text("q")
            }
        }

    }
}
