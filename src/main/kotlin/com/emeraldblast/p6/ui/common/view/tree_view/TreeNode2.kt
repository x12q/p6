package com.emeraldblast.p6.ui.common.view.tree_view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.LayoutCoorsUtils.ifAttached
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.rms
import com.emeraldblast.p6.ui.common.compose.TestApp
import com.emeraldblast.p6.ui.common.compose.LayoutCoorsUtils.wrap
import com.emeraldblast.p6.ui.common.view.MBox
import com.emeraldblast.p6.ui.common.view.tree_view.state.TreeNodeState
import com.emeraldblast.p6.ui.common.view.tree_view.state.TreeNodeStateImp

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun TreeNode2(
//    isExpandable: Boolean = true,
//    isExpanded: Boolean = false,
//    contextMenuItems: List<ContextMenuItem> = emptyList(),
//    onClick: () -> Unit = {},
//    onDoubleClick: () -> Unit = {},
//    expandIcon: @Composable (isExpanded: Boolean) -> Unit = {
//        if (isExpandable) {
//            MBox(
//                modifier = Modifier.background(
//                    Color.Green
//                ).size(15.dp, 15.dp)
//            )
//        }
//    },
//    collapseIcon: @Composable (isExpanded: Boolean) -> Unit = {
//        if (isExpandable) {
//            MBox(
//                modifier = Modifier.background(
//                    Color.Gray
//                ).size(15.dp, 15.dp)
//            )
//        }
//    },
//    itemIcon: @Composable () -> Unit = {},
//    nodeText: String,
//    children: @Composable () -> Unit = {}
//) {
//    TreeNode(
//        isExpandable = isExpandable,
//        isExpanded = isExpanded,
//        onClick = onClick,
//        onDoubleClick = onDoubleClick,
//        expandIcon = expandIcon,
//        collapseIcon = collapseIcon,
//        itemIcon = itemIcon,
//        nodeContent = {
//            BasicText(nodeText)
//        },
//        children = children,
//        contextMenuItems = contextMenuItems
//    )
//}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TreeNode2(
    stateMs: Ms<TreeNodeState>,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    contextMenuItems: List<ContextMenuItem> = emptyList(),
    nodeText: String,
    expandIcon: @Composable () -> Unit = {
        MBox(
            modifier = Modifier.background(
                Color.Green
            ).size(15.dp, 15.dp)
        )
    },
    collapseIcon: @Composable () -> Unit = {
        MBox(
            modifier = Modifier.background(
                Color.Gray
            ).size(15.dp, 15.dp)
        )
    },
    itemIcon: @Composable () -> Unit = {},
    children: @Composable () -> Unit = {}
) {
    TreeNode2(
        stateMs = stateMs,
        onClick = onClick,
        onDoubleClick = onDoubleClick,
        contextMenuItems = contextMenuItems,
        nodeContent = {
            BasicText(nodeText)
        },
        expandIcon = expandIcon,
        collapseIcon = collapseIcon,
        itemIcon = itemIcon,
        children = children
    )
}

/**
 * A tree node consists of:
 *  - an optional expanding icon denoting expanding state
 *  - an optional item icon
 *  - an optional item content
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TreeNode2(
    stateMs: Ms<TreeNodeState>,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    contextMenuItems: List<ContextMenuItem> = emptyList(),
    nodeContent: @Composable () -> Unit = {
        BasicText(text = "A tree node")
    },
    expandIcon: @Composable () -> Unit = {
        MBox(
            modifier = Modifier.background(
                Color.Green
            ).size(15.dp, 15.dp)
        )
    },
    collapseIcon: @Composable () -> Unit = {
        MBox(
            modifier = Modifier.background(
                Color.Gray
            ).size(15.dp, 15.dp)
        )
    },
    itemIcon: @Composable () -> Unit = {},
    children: @Composable () -> Unit = {}
) {
    var state by stateMs
    Column {
        // x: node item view.
        MBox(
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        state = state.switchExpanded()
                        onClick()
                    },
                    onDoubleClick = {
                        onDoubleClick()
                    },
                ).onGloballyPositioned {
                    it.ifAttached { i ->
                        state.setLayoutCoorWrapper(i.wrap())
                    }
                }
        ) {
            ContextMenuArea(items = { contextMenuItems }) {
                Row {
                    MBox {
                        if (stateMs.value.isExpandable) {
                            if (stateMs.value.isExpanded) {
                                expandIcon()
                            }
                            if (!stateMs.value.isExpanded) {
                                collapseIcon()
                            }
                        }
                    }
                    itemIcon()
                    nodeContent()
                }
            }
        }
        if (state.isExpanded) {
            MBox(
                modifier = Modifier.offset(x = 33.dp)
            ) {
                children()
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
private fun main() {
    TestApp(
        size = DpSize(100.dp, 300.dp)
    ) {
        val s: Ms<TreeNodeState> = rms(TreeNodeStateImp(isExpandable = true, isExpanded = false))
        TreeNode2(
            stateMs = s,
            nodeText = "QWE"
        ) {
            Column {
                Text("ABC")
                Text("123")
            }
        }
    }
}



