package com.qxdzbc.p6.ui.app.cell_editor.in_cell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
        sc.cellEditorState.isActive = true
        MBox(modifier= Modifier.border(1.dp,color= Color.Blue).size(300.dp).background(Color.Yellow)){
            CellEditorView(
                state = sc.cellEditorState,
                action = p6Comp.cellEditorAction(),
                isFocused = true,
            )
        }
    }
}
