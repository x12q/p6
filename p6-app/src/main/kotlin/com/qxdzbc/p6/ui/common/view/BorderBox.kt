package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.common.compose.view.testApp


/**
 * A Box that provides a more customized border feature.
 *
 * [padContent]: whether content is overlapped by the border or not. By default, this property is false (that means no overlapping between border and content) to mimic the behavior the provided compose border box.
 */
@Composable
fun BorderBox(
    modifier: Modifier = Modifier,
    borderStyle: BorderStyle = BorderStyle.ALL,
    borderColor: Color = Color.Black,
    borderThickness: Int = 1,
    padContent:Boolean = false,
    content: @Composable BoxScope.() -> Unit = {},
) {

    MBox(
        modifier = modifier.drawWithContent {
            drawContent()
            val xWidth = size.width
            val yHeight = size.height
            val t = borderThickness.toFloat()
            if(t >= size.width && t >= size.height){
                drawRect(
                    color = borderColor,
                )
            }else{
                val sideThickness = minOf(t,xWidth)
                val botTopThickness = minOf(t,yHeight)
                if (BorderStyleValue.__TOP in borderStyle) {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, 0f+botTopThickness/2), // top left
                        end = Offset(xWidth, 0f+botTopThickness/2), // top right
                        strokeWidth = botTopThickness,

                        )
                }
                if (BorderStyleValue.__LEFT in borderStyle) {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f+sideThickness/2, 0f), //top left
                        end = Offset(0f+sideThickness/2, yHeight), //bot left
                        strokeWidth = sideThickness
                    )
                }
                if (BorderStyleValue.__RIGHT in borderStyle) {
                    drawLine(
                        color = borderColor,
                        start = Offset(xWidth-sideThickness/2, 0f), // top right
                        end = Offset(xWidth-sideThickness/2, yHeight), // bot right
                        strokeWidth = sideThickness
                    )
                }
                if (BorderStyleValue.__BOT in borderStyle) {
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, yHeight-botTopThickness/2), // bot left
                        end = Offset(xWidth, yHeight-botTopThickness/2), // bot right
                        strokeWidth = botTopThickness
                    )
                }
            }
        }
    ) {
        if(padContent){
            MBox(modifier=Modifier.padding(
                top = (if(BorderStyleValue.__TOP in borderStyle) borderThickness else 0).dp,
                bottom = (if(BorderStyleValue.__BOT in borderStyle) borderThickness else 0).dp,
                start = (if(BorderStyleValue.__LEFT in borderStyle) borderThickness else 0).dp,
                end = (if(BorderStyleValue.__RIGHT in borderStyle) borderThickness else 0).dp,
            )){
                content()
            }
        }else{
            content()
        }
    }
}

fun main() = testApp {
    val size = DpSize(200.dp, 100.dp)
    Column(modifier = Modifier.padding(20.dp)) {
        MBox(modifier = Modifier.size(size).border(10.dp, Color.Black)){
            Text("ABC")
        }
        Row (modifier = Modifier.padding(top=30.dp, bottom =10.dp,)) {
            BorderBox(borderStyle = BorderStyle.TOP_LEFT,
                modifier = Modifier.padding(end=10.dp).size(size).background(Color.Cyan),
                borderColor = Color.Black.copy(alpha =0.7f),
                padContent = true,
                borderThickness = 80) {
                Text("ABC")
            }
            MBox(modifier = Modifier.size(size).border(10.dp, Color.Black)){
                Text("ABC")
            }
        }

        BorderBox(borderStyle = BorderStyle.BOT_RIGHT, modifier = Modifier.size(size).background(Color.Cyan), borderThickness = 10,padContent = false,) {
            Text("ABC")
        }
    }
}
