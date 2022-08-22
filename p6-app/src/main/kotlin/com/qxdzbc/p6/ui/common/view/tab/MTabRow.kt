package com.qxdzbc.p6.ui.common.view.tab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.TestApp

@Composable
fun MTabRow(
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit,
) {
    // TODO this default implementation does not allow custom min tab width. May need to implement a copy in the future.
    ScrollableTabRow(
        edgePadding = 0.dp,
        backgroundColor = Color.Transparent,
        selectedTabIndex = selectedIndex,
        modifier = modifier.height(30.dp),
        indicator = { tabPosList ->
//            if (tabPosList.isNotEmpty() && selectedIndex in 0 .. tabPosList.size) {
//                MBox(
//                    modifier = Modifier.tabIndicatorOffset(tabPosList[selectedIndex])
//                        .height(4.dp)
//                        .background(MaterialTheme.colors.secondary)
//                )
//            }
        }
    ) {
        tabs()
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun main() {
    TestApp {
        val ls = (1..20).map { it }
        var si by rms(0)
        MTabRow(
            selectedIndex = si,
            tabs = {
                for ((i, l) in ls.withIndex()) {
                        Tabs.MTabWithCloseButton(
                            text = "tffffffffffffaffffffffffffffffffffffb $l",
                            onClick = { si = i },
                            isSelected = si == i
                        )
                    }

            }
        )
    }
}
