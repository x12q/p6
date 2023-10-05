package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme

/**
 * The need to separate the thumb and thumb content is that:
 * - Thumb act as an invisible container that handles all the sliding logic
 * - Thumb content act as the appearance of the thumb, only handle the aesthetic
 */
@Composable
fun ThumbContent(
    modifier: Modifier = Modifier
) {
    MBox(
        modifier.padding(3.dp).fillMaxSize().background(P6Theme.color.uiColor.sliderThumbContentColor)
    )
}