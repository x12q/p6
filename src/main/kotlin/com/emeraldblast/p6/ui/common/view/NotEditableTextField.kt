package com.emeraldblast.p6.ui.common.view

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotEditableTextField(
    text:String,
    modifier:Modifier = Modifier
){
    BasicTextField(
        value =text,
        onValueChange = {},
        modifier=modifier
    )
}
