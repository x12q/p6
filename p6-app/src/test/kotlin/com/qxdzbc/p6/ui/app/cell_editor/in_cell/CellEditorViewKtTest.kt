package com.qxdzbc.p6.ui.app.cell_editor.in_cell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.app.cell_editor.CellEditorView
import test.BaseTest
import test.testRunApp

internal class CellEditorViewKtTest: BaseTest(){
}
fun main(){
    testRunApp {
        sc.cellEditorState.isActiveMs.value = true
        MBox(modifier= Modifier.padding(30.dp).border(1.dp,color= Color.Blue).background(Color.Yellow)){
            CellEditorView(
                state = sc.cellEditorState,
                action = p6Comp.cellEditorAction(),
                isFocused = true,
            )
        }
    }
}
