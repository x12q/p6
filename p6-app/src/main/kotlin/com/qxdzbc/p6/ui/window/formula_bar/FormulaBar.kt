package com.qxdzbc.p6.ui.window.formula_bar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox

@Composable
fun FormulaBar(
    state:FormulaBarState
){
    MBox(Modifier.fillMaxSize()){
        BasicText(text =state.text,modifier = Modifier.align(Alignment.CenterStart))
    }
}
