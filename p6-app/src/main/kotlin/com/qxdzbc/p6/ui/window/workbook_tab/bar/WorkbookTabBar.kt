package com.qxdzbc.p6.ui.window.workbook_tab.bar

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
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.workbook.WorkbookConstants
import com.qxdzbc.p6.ui.window.workbook_tab.tab.WorkbookTab

@Composable
fun WorkbookTabBar(
    state: WorkbookTabBarState,
    action: WorkbookTabBarAction,
) {
    MBox(modifier = Modifier.fillMaxSize()) {
        Row {
            LazyRow(modifier = Modifier.weight(1.0F)) {
                items(
                    items=state.tabStateList,
                    key={wbTabState->wbTabState.wbKey},
                    itemContent={tabState->
                        MBox(
                            modifier = Modifier
                                .requiredWidthIn(
                                    WorkbookConstants.minWorkbookTabWidth,
                                    WorkbookConstants.maxWorkbookTabWidth
                                )
                                .height(WorkbookConstants.tabHeight)
                        ) {
                            WorkbookTab(
                                tabState,
                                onClick = {
                                    action.moveToWorkbook(it)
                                },
                                onClose = {
                                    action.close(it, state.windowId)
                                },
                                onClickSave={
                                    action.openSaveFileDialog(state.windowId)
                                }
                            )
                        }
                    },
                )
            }

            // x: this is the button to create new workbook
            BorderBox(
                borderStyle = BorderStyle.LEFT_RIGHT,
                modifier = Modifier
                    .width(WorkbookConstants.tabHeight)
                    .height(WorkbookConstants.tabHeight)
                    .align(Alignment.Bottom)
                    .clickable { action.createNewWb(state.windowId) }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add new workbook",
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}
