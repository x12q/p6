package com.emeraldblast.p6.ui.example

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.testApp
import com.emeraldblast.p6.ui.common.view.BorderBox
import com.emeraldblast.p6.ui.common.view.MBox

fun main() {
    testApp {
        val st = rememberScrollState(0)
        val rst = rememberScrollState(0)
        MBox(
            modifier = Modifier.fillMaxSize()
                .wrapContentSize(unbounded = true, align = Alignment.TopStart)
        ) {
            Column {
                Box {
                    BasicText("Title")
                }
                Row {
                    Row(
                        modifier = Modifier.wrapContentSize(unbounded = true, align = Alignment.TopStart)
                    ) {
                        val ls = (1..100).toList()
                        for (l in ls) {
                            val cs = (1..100).toList()
                            Column(
                                modifier = Modifier
                                    .wrapContentSize(unbounded = true, align = Alignment.TopStart)
                            ) {
                                for (i in cs) {
                                    BorderBox(modifier = Modifier.size(150.dp, 40.dp)) {
                                        MBox(modifier = Modifier.fillMaxSize()) {
                                            BasicText("Number ${i}................")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
