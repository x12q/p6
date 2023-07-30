package com.qxdzbc.p6.ui.theme.typography

import androidx.compose.material.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.qxdzbc.p6.ui.theme.color.WsColor


class P6TypoGraphy(
    val button:TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    val body1:TextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
){
    companion object {

        fun light():P6TypoGraphy{
            return P6TypoGraphy()
        }
        fun dark():P6TypoGraphy{
            return P6TypoGraphy(
                // TODO add dark color
            )
        }


        val local = staticCompositionLocalOf(
            defaultFactory = {
                light()
            }
        )
    }
}

//val P6DefaultTypoGraphy = Typography(
//    button = TextStyle(
//        fontFamily = FontFamily.SansSerif,
//        fontWeight = FontWeight.Normal,
//        fontSize = 13.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    ),
//    body1 = TextStyle(
//        fontFamily = FontFamily.SansSerif,
//        fontWeight = FontWeight.Normal,
//        fontSize = 14.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    ),
//)
//
