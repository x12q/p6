package com.qxdzbc.p6.ui.window.workbook_tab.bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.R
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.common.view.MBox
import com.qxdzbc.p6.ui.window.workbook_tab.tab.WorkbookTabView

@Composable
fun WorkbookTabBarView(
    state: WorkbookTabBarState,
    wbTabBarActions: WorkbookTabBarAction,
) {
    val stateHorizontal = rememberScrollState(0)

    MBox(modifier = Modifier.fillMaxSize()) {
        Row {
            MBox(modifier = Modifier.weight(1.0F).horizontalScroll(stateHorizontal)) {
                Row {
                    for (tabState in state.tabStateList) {
                        MBox(
                            modifier = Modifier
                                .requiredWidthIn(
                                    R.size.value.minWorkbookTabWidth.dp,
                                    R.size.value.maxWorkbookTabWidth.dp
                                )
                                .height(R.size.value.tabHeight.dp)
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
                    }
                }
            }

            // x: this is the button to create new workbook
            BorderBox(
                style = BorderStyle.LEFT_RIGHT,
                modifier = Modifier
                    .width(R.size.value.tabHeight.dp)
                    .height(R.size.value.tabHeight.dp)
                    .align(Alignment.Bottom)
                    .clickable { wbTabBarActions?.createNewWb(state.windowId) }
//                    .background(MaterialTheme.colors.secondaryVariant)
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
