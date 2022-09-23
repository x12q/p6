package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.p6R
import com.qxdzbc.p6.ui.common.compose.TestApp

/**
 * Border text field
 */
@Composable
fun SingleLineInputText(
    text: String,
    onValueChange: (newText: String) -> Unit = {},
    modifier: Modifier = Modifier,
    selectAll: Boolean = false,
    enabled: Boolean = true,
    textFq: FocusRequester? = null,
) {
    val borderColor = MaterialTheme.colors.onPrimary
    MBox(
        modifier = Modifier
            .then(modifier)
            .clip(p6R.shape.textFieldShape)
            .border(1.dp, borderColor, p6R.shape.textFieldShape)
            .padding(5.dp)
    ) {
        UseP6TextSelectionColor {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                maxLines = 1,
                modifier = Modifier.align(Alignment.CenterStart)
                    .fillMaxSize()
                    .then(if (textFq != null) Modifier.focusRequester(textFq) else Modifier),
                enabled = enabled
            )
        }
    }
}

fun main() {
    TestApp {
        SingleLineInputText("TExt")
    }
}
