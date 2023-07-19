package com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.document.workbook.WorkbookConstants
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.tab.SheetTabView

@Composable
fun SheetTabBarView(
    state: SheetTabBarState,
    onItemClick: (sheetName: String) -> Unit = {},
    onNewButtonClick: () -> Unit = {},
    onRename: (sheetName: String) -> Unit = {},
    onDelete: (sheetName: String) -> Unit = {},
) {
    MBox(modifier = Modifier.fillMaxSize()) {
        Row {
            LazyRow(modifier = Modifier.weight(1.0F)) {
                items(
                    items=state.tabStateList,
                    key={tabState->tabState.sheetName},
                    itemContent={tabState->
                        MBox(modifier = Modifier
                            .align(Alignment.Bottom)
                            .requiredWidthIn(WorkbookConstants.minTabWidth, WorkbookConstants.maxTabWidth)
                            .height(WorkbookConstants.tabHeight)
                        ) {
                            SheetTabView(state=tabState, onClick = onItemClick,
                                onRename = onRename, onDelete = onDelete,
                            )
                        }
                    }
                )
            }
//            val stateHorizontal = rememberScrollState(0)
//            MBox(modifier = Modifier.weight(1.0F)
//                .horizontalScroll(stateHorizontal)
//            ) {
//                Row {
//                    for (tabState in state.tabStateList) {
//                        MBox(modifier = Modifier
//                            .align(Alignment.Bottom)
//                            .requiredWidthIn(p6R.size.value.minTabWidth.dp, p6R.size.value.maxTabWidth.dp)
//                            .height(p6R.size.value.tabHeight.dp)
//                        ) {
//                            SheetTabView(state=tabState, onClick = onItemClick,
//                                onRename = onRename, onDelete = onDelete,
//                            )
//                        }
//                    }
//                }
//            }

            BorderBox(
                style = BorderStyle.LEFT_RIGHT,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .align(Alignment.Bottom)
                    .clickable { onNewButtonClick() }
//                    .background(MaterialTheme.colors.secondaryVariant)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add worksheet",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}
