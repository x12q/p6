package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType

@Composable
internal fun Thumb(
    length: Dp,
    offset: DpOffset,
    type: ScrollBarType,
    modifier: Modifier = Modifier,
    color: Color = P6Theme.color.uiColor.sliderThumbBackgroundColor,
    thumbContent: @Composable () -> Unit = {
        ThumbContent()
    },
) {

    val sizeMod = when (type) {
        ScrollBarType.Vertical -> Modifier
            .height(length)
            .fillMaxWidth()

        ScrollBarType.Horizontal -> Modifier
            .width(length)
            .fillMaxHeight()
    }

    MBox(
        modifier = Modifier
            .offset(x = offset.x, y = offset.y)
            .then(sizeMod)
            .background(color)
            .then(modifier)

    ) {
        thumbContent()
    }
}

@Preview
@Composable
fun Preview_Slider() {

    MBox(Modifier.size(30.dp, 150.dp)) {
        Thumb(20.dp, DpOffset(20.dp, 30.dp), type = ScrollBarType.Vertical)
    }


}