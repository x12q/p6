package com.qxdzbc.p6.ui.common.view.m_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qxdzbc.common.compose.view.MBox


@Composable
fun MenuScope.MenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val upperScope = this
    MBox(modifier = modifier.clickable {
        onClick()
        upperScope.close()
    }){
        content()
    }
}

@Composable
fun MenuScope.MenuItem(label:String, modifier: Modifier= Modifier, onClick: () -> Unit){
    MenuItem(onClick,modifier){
        BasicText(label)
    }
}
