package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox

@Composable
fun PlaceHolder(str: String) {
    MBox(modifier = Modifier.fillMaxSize()) {
        Text(str, modifier = Modifier.align(Alignment.Center))
    }
}

