package com.qxdzbc.p6.ui.window.workbook_tab.tab

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import com.qxdzbc.common.compose.view.testApp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.view.*
import com.qxdzbc.p6.ui.theme.common.P6CommonUIModifiers
import com.qxdzbc.p6.ui.theme.P6Theme
import com.qxdzbc.p6.ui.window.dialog.AskSaveDialog
import com.qxdzbc.p6.ui.window.workbook_tab.tab.components.CloseWorkbookButton
import com.qxdzbc.p6.ui.window.workbook_tab.tab.components.WorkbookTabNameText

/**
 * A workbook tab view. Signify a tab that user can click on to open a workbook.
 * This view contains:
 * - name of a workbook.
 * - a close button to close a workbook.
 * - a close dialog asking user to save the workbook before closing.
 * - a context menu when users right-click on the tab.
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WorkbookTabView(
    state: WorkbookTabState,
    internalState: WorkbookTabInternalState = WorkbookTabInternalStateImp(),
    modifier: Modifier = Modifier,
    onClick: (workbookKey: WorkbookKey) -> Unit = {},
    onClose: (workbookKey: WorkbookKey) -> Unit = {},
    onClickSave: () -> Unit = {},
) {

    ContextMenuArea(items = {
        listOf(
            ContextMenuItem("Close") { onClose(state.wbKey) },
        )
    }) {
        BoolBackgroundBox(
            boolValue = state.isSelected,
            colorIfTrue = P6Theme.color.uiColor.selectedTabBackground,
            modifier = modifier
                .fillMaxHeight()
                .clickable { onClick(state.wbKey) }
                .onPointerEvent(PointerEventType.Enter) {
                    internalState.mouseOnTab = true
                }
                .onPointerEvent(PointerEventType.Exit) {
                    internalState.mouseOnTab = false
                }
        ) {
            BorderBox(
                borderStyle = BorderStyle.RIGHT,
            ) {
                CenterAlignRow(
                    modifier = P6CommonUIModifiers.smallBoxPadding
                        .fillMaxHeight()
                ) {

                    WorkbookTabNameText(state.tabName)

                    CloseWorkbookButton(
                        onClick={
                            if (state.needSave) {
                                internalState.openAskToSaveDialog = true
                            } else {
                                onClose(state.wbKey)
                            }
                        }
                    )
                }
            }
        }
    }

    if (internalState.openAskToSaveDialog) {
        AskSaveDialog(
            state.tabName,
            onCancel = {
                internalState.openAskToSaveDialog = false
            },
            onDontSave = {
                internalState.openAskToSaveDialog = false
                onClose(state.wbKey)
            },
            onSave = {
                internalState.openAskToSaveDialog = false
                onClickSave()
            }
        )
    }
}


fun main(){
    testApp {
        WorkbookTabView(state = WorkbookTabStateImp.random())
    }
}