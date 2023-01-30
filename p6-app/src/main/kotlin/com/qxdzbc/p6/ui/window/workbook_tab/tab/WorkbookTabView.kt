package com.qxdzbc.p6.ui.window.workbook_tab.tab

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.BoolBackgroundBox
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle
import com.qxdzbc.p6.ui.window.dialog.AskSaveDialog


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WorkbookTabView(
    state: WorkbookTabState,
    internalStateMs: Ms<WorkbookTabInternalState> = rms(WorkbookTabInternalStateImp()),
    modifier: Modifier = Modifier,
    onClick: (workbookKey: WorkbookKey) -> Unit = {},
    onClose: (workbookKey: WorkbookKey) -> Unit = {},
    onClickSave: () -> Unit = {},
) {

    val openAskToSaveDialog = internalStateMs.value.openAskToSaveDialog
    var internalState by internalStateMs

    ContextMenuArea(items = {
        listOf(
            ContextMenuItem("Close") { onClose(state.wbKey) },
        )
    }) {
        BoolBackgroundBox(
            boolValue = state.isSelected,
            modifier = modifier
                .fillMaxHeight()
                .clickable { onClick(state.wbKey) }
                .onPointerEvent(PointerEventType.Enter) {
                    internalState = internalState.setMouseOnTab(true)
                }
                .onPointerEvent(PointerEventType.Exit) {
                    internalState = internalState.setMouseOnTab(false)
                }
        ) {
            BorderBox(
                style = BorderStyle.RIGHT,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .then(P6R.text.mod.smallBoxPadding)
                ) {
                    Text(
                        text = state.tabName,
                        modifier = Modifier
                            .then(P6R.text.mod.smallBoxPadding),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    BorderBox(
                        style = BorderStyle.ALL,
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .size(DpSize(18.dp, 18.dp))
                            .clickable {
                                if (state.needSave) {
                                    internalState = internalState.setOpenAskToSaveDialog(true)
                                } else {
                                    onClose(state.wbKey)
                                }
                            }

                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Localized description"
                        )
                    }
                }
            }
        }
    }

    if (openAskToSaveDialog) {
        AskSaveDialog(
            state.tabName,
            onCancel = {
                internalState = internalState.setOpenAskToSaveDialog(false)
            },
            onDontSave = {
                internalState = internalState.setOpenAskToSaveDialog(false)
                onClose(state.wbKey)
            },
            onSave = {
                internalState = internalState.setOpenAskToSaveDialog(false)
                onClickSave()
            }
        )
    }
}
