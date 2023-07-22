package com.qxdzbc.p6.ui.document.workbook.sheet_tab.tab

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.modifier.boolBackground
import com.qxdzbc.p6.ui.theme.common.P6CommonUIModifiers
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.theme.P6Theme

@Composable
fun SheetTabView(
    state: SheetTabState,
    modifier: Modifier = Modifier,
    onClick: (sheetName: String) -> Unit = {},
    onRename: (sheetName: String) -> Unit = {},
    onDelete: (sheetName: String) -> Unit = {},
) {

    ContextMenuArea(items = {
        listOf(
            ContextMenuItem("Rename") { onRename(state.sheetName) },
            ContextMenuItem("Delete") { onDelete(state.sheetName) }
        )
    }) {

        BorderBox(
            borderStyle = BorderStyle.RIGHT,
            modifier = modifier
                .fillMaxHeight()
                .clickable { onClick(state.sheetName) }
                .boolBackground(
                    boolValue = state.isSelected,
                    colorIfTrue = P6Theme.color.uiColor.selectedTabBackground,
                )
        ) {
            Text(
                state.sheetName,
                modifier = Modifier
                    .align(Alignment.Center)
                    .then(P6CommonUIModifiers.smallBoxPadding),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


fun main() {
    singleWindowApplication {
        Column {
            Box(modifier = Modifier.size(100.dp, 30.dp)) {
                SheetTabView(SheetTabStateImp("abc", true))
            }
            Box(modifier = Modifier.size(100.dp, 30.dp)) {
                SheetTabView(SheetTabStateImp("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", false))
            }
        }

    }
}
