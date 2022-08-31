package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.rpc.P6RpcServerImp
import com.qxdzbc.p6.rpc.document.cell.MockCellService
import com.qxdzbc.p6.rpc.document.workbook.MockWorkbookService
import com.qxdzbc.p6.ui.common.color_generator.ColorGeneratorImp
import com.qxdzbc.p6.ui.common.compose.TestApp

fun main(){
    TestApp{
        val cg = remember { ColorGeneratorImp() }
        var color  by rms(cg.nextColor())
        val str:AnnotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append("H")
            }
            append("ello ")
        }

        val str2:AnnotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                append("W")
            }
            append("orld")
        }
        val str3=str + str2
        Column{
            Box(
                modifier = Modifier.size(200.dp).background(color)
            ){
                Text(
                    text = str3.subSequence(0,4)
                )
            }
            Button("C") {
                color = cg.nextColor()
                println(color)
            }
        }

    }
}
