package com.qxdzbc.p6.ui.window.workbook_tab.tab

import androidx.compose.foundation.*
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
import androidx.compose.runtime.remember
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
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.common.view.BoolBackgroundBox
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun WorkbookTabView(
    state: WorkbookTabState,
    modifier: Modifier = Modifier,
    onClick: (workbookKey: WorkbookKey) -> Unit = {},
    onClose: (workbookKey: WorkbookKey) -> Unit = {},
) {
    var mouseOnTab: Boolean by remember { ms(false) }
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
                    mouseOnTab = true
                }
                .onPointerEvent(PointerEventType.Exit) {
                    mouseOnTab = false
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
                                onClose(state.wbKey)
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
}
