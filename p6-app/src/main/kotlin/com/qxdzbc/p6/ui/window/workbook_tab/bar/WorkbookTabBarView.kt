package com.qxdzbc.p6.ui.window.workbook_tab.bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.common.compose.view.MBox
import com.qxdzbc.p6.ui.window.dialog.ask_to_save_dialog.AskSaveDialog
import com.qxdzbc.p6.ui.window.workbook_tab.tab.WorkbookTabView

@Composable
fun WorkbookTabBarView(
    state: WorkbookTabBarState,
    wbTabBarActions: WorkbookTabBarAction,
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
                                    P6R.size.value.minWorkbookTabWidth.dp,
                                    P6R.size.value.maxWorkbookTabWidth.dp
                                )
                                .height(P6R.size.value.tabHeight.dp)
                        ) {
                            WorkbookTabView(
                                tabState,
                                onClick = {
                                    wbTabBarActions.moveToWorkbook(it)
                                },
                                onClose = {
                                    wbTabBarActions.close(it, state.windowId)
                                }
                            )
                        }
                    },
                )
            }

            // x: this is the button to create new workbook
            BorderBox(
                style = BorderStyle.LEFT_RIGHT,
                modifier = Modifier
                    .width(P6R.size.value.tabHeight.dp)
                    .height(P6R.size.value.tabHeight.dp)
                    .align(Alignment.Bottom)
                    .clickable { wbTabBarActions.createNewWb(state.windowId) }
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
