package com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme
import com.qxdzbc.p6.ui.worksheet.slider.scroll_bar.state.ScrollBarType

@Composable
internal fun Rail(
    thickness: Dp = 30.dp,
    modifier: Modifier = Modifier,
    color: Color = P6Theme.color.uiColor.sliderRailBackground,
    type:ScrollBarType,
    content: @Composable () -> Unit,
) {

    val sizeMod = when(type){
        ScrollBarType.Vertical -> Modifier
            .width(thickness)
            .fillMaxHeight()

        ScrollBarType.Horizontal -> Modifier
            .height(thickness)
            .fillMaxWidth()
    }

    MBox(
        Modifier
            .then(sizeMod)
            .background(color)
            .then(modifier)
    ) {
        content()
    }
}


@Preview
@Composable
fun Preview_SliderRail() {
    MBox(Modifier.size(30.dp,100.dp)) {
        Rail(type=ScrollBarType.Vertical){}
    }

}