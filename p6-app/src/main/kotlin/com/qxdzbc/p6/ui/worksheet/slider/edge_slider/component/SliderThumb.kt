package com.qxdzbc.p6.ui.worksheet.slider.edge_slider.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.theme.P6Theme

@Composable
internal fun SliderThumb(
    length: Dp,
    offset: DpOffset,
    modifier: Modifier = Modifier,
    color: Color = P6Theme.color.uiColor.sliderThumbColor
) {
    MBox(
        modifier = Modifier
            .offset(offset.x,offset.y)
            .height(length)
            .fillMaxWidth()
            .padding(horizontal = 3.dp)
            .background(color)
            .then(modifier)
    )
}

@Preview
@Composable
fun Preview_Slider() {

    MBox(Modifier.size(30.dp,150.dp)) {
        SliderThumb(20.dp, DpOffset(20.dp,30.dp))
    }


}