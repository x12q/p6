package com.qxdzbc.p6.ui.example


import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.singleWindowApplication

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)


fun main() {
    singleWindowApplication(title = "Context menu") {

        Row {
            ContextMenuDataProvider(items = {
                listOf(
                    ContextMenuItem("Option 1"){},
                ContextMenuItem("Option 2"){}
                )
            }) {
                SelectionContainer {
                    Text("T1")
                }

                SelectionContainer {
                    Text("T2")
                }
            }
        }
    }
}
