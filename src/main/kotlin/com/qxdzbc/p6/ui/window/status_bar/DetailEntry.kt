package com.qxdzbc.p6.ui.window.status_bar

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.view.NotEditableTextField

@Composable
fun DetailEntry(text: String) {
    NotEditableTextField(
        text,
        modifier = Modifier.padding(top = 5.dp)
    )
}
