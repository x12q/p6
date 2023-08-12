package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme

@Composable
internal fun SliderRail(
    width: Dp = 20.dp,
    modifier: Modifier = Modifier,
    color: Color = P6Theme.color.uiColor.sliderRailBackground,
    content: @Composable () -> Unit,
) {

    MBox(
        Modifier
            .width(width)
            .fillMaxHeight()
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
        SliderRail{}
    }

}