package com.qxdzbc.p6.ui.common.view.tab

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.view.BoolBackgroundBox
import com.qxdzbc.p6.ui.common.view.BorderBox
import com.qxdzbc.p6.ui.common.view.BorderStyle

object Tabs {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MTabWithCloseButton(
        text: String,
        onClick: () -> Unit = {},
        isSelected: Boolean = false,
        modifier: Modifier = Modifier,
        onClose: () -> Unit = {},
    ) {
        MTab(
            isSelected = isSelected,
            onClick = onClick,
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .then(P6R.text.mod.smallBoxPadding)
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .requiredWidthIn(P6R.size.value.minTabWidth2.dp - 43.dp, P6R.size.value.maxTabWidth.dp - 43.dp),
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
                            onClose()
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


    /**
     * Pre-config tab for common use
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MTextTab(
        text: String,
        onClick: () -> Unit = {},
        isSelected: Boolean = false,
        modifier: Modifier = Modifier,
        contextMenuItems: List<ContextMenuItem> = emptyList()
    ) {
        MTab(
            isSelected = isSelected,
            onClick = onClick,
            modifier = modifier,
            contextMenuItems = contextMenuItems
        ) {
            Text(
                text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .then(P6R.text.mod.smallBoxPadding),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

    /**
     * Pre-config tab for common use
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MTab(
        isSelected: Boolean = false,
        onClick: () -> Unit = {},
        modifier: Modifier = Modifier,
        contextMenuItems: List<ContextMenuItem> = emptyList(),
        content: @Composable BoxScope.() -> Unit,
    ) {
        ContextMenuArea(items = { contextMenuItems }) {
            BoolBackgroundBox(
                boolValue = isSelected,
                modifier = modifier
                    .fillMaxHeight()
                    .requiredWidthIn(P6R.size.value.minTabWidth2.dp, P6R.size.value.maxTabWidth.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = onClick
                    )
            ) {
                BorderBox(
                    style = BorderStyle.RIGHT,
                    modifier = Modifier
                        .fillMaxHeight()
                        .requiredWidthIn(P6R.size.value.minTabWidth2.dp, P6R.size.value.maxTabWidth.dp)
                ) {
                    content(this@BorderBox)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun main() {
    singleWindowApplication {
        Column {
            Box(modifier = Modifier.size(500.dp, 30.dp)) {
                Tabs.MTextTab("abc")
            }
            Box(modifier = Modifier.size(500.dp, 30.dp)) {
                Tabs.MTextTab("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", contextMenuItems = listOf(
                    ContextMenuItem("abc") {}
                ))
            }

            Box(modifier = Modifier.size(500.dp, 30.dp)) {
                Tabs.MTabWithCloseButton(
                    "q",
                    isSelected = false,
                    onClose = { println("on close") })
            }
        }
    }
}
